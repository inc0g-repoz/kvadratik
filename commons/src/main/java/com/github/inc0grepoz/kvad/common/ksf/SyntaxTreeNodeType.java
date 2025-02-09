package com.github.inc0grepoz.kvad.common.ksf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.github.inc0grepoz.kvad.common.utils.Logger;

public enum SyntaxTreeNodeType {

    COMMENT(
        null, null,
        tn -> tn.line.startsWith("//"),
        tn -> new PipeOther() {

            @Override
            boolean execute(VarPool vp) {
                return true;
            }

        }
    ),
    EVENT(
        "(" + Keyword.EVENT + " )(.+)\\(.+\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.EVENT + " "),
        tn -> {
            String[] args = tn.line.split("(^" + Keyword.EVENT + " )|\\(|\\)");
            return new PipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        "(" + Keyword.FOR + " ?)\\((.+=.+)?;.*;.*\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.FOR.toString()) &&
              tn.line.contains("(") &&
              tn.line.contains(")") &&
              tn.line.contains(";"),
        tn -> {
            String[] ols = Expressions.oneLineStatement(tn.line);
            String[] params = ols[0].split(";");

            PipeRef ref = PipeRef.resolve(params[0]);
            Var boolExp = Expressions.resolveVar(params[1]);
            Var op = Expressions.resolveVar(params[2]);

            if (tn.children.isEmpty()) {
                tn.firstScopeMember().write(ols[1]);
                tn.defineNodeTypes_r();
            }
            return new PipeFor(ref, boolExp, op);
        }
    ),
    FOR_EACH(
        "(" + Keyword.FOR + " ?)\\(.+:.+\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.FOR.toString()) &&
              tn.line.contains("(") &&
              tn.line.contains(")") &&
              tn.line.contains(":"),
        tn -> {
            String[] ols = Expressions.oneLineStatement(tn.line);
            String[] params = ols[0].split(":");

            List<String> typeParam = new ArrayList<>();
            for (String w : params[0].split(" +")) {
                if (!w.isEmpty()) {
                    typeParam.add(w);
                }
            }

            if (tn.children.isEmpty()) {
                tn.firstScopeMember().write(ols[1]);
                tn.defineNodeTypes_r();
            }
            return new PipeForEach(typeParam.get(1), Expressions.resolveVar(params[1]));
        }
    ),
    IF(
        "(" + Keyword.IF + " ?)\\(.+\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.IF.toString()) &&
              tn.line.contains("(") &&
              tn.line.contains(")"),
        tn -> {
            if (tn.children.isEmpty()) {
                String[] ols = Expressions.oneLineStatement(tn.line);
                tn.firstScopeMember().write(ols[1]);
                tn.defineNodeTypes_r();
                return new PipeIf(Expressions.resolveVar(ols[0]));
            } else {
                String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
                return new PipeIf(Expressions.resolveVar(exp));
            }
        }
    ),
    // TODO: Get rid of duplicate `else` statements
    ELSE(
        null, null,
        tn -> tn.line.startsWith(Keyword.ELSE.toString()),
        tn -> {
            if (tn.prev == null || tn.prev.type != IF) {
                throw new IllegalStateException("Else statements need to be put after if statements");
            }
            if (tn.children.isEmpty()) {
                String ols = tn.line.substring(Keyword.ELSE.toString().length());
                tn.firstScopeMember().write(ols);
                tn.defineNodeTypes_r();
            }
            PipeIf ifSt = (PipeIf) tn.prev.compiled;
            return ifSt.elsePipe = new PipeOther() {

                @Override
                boolean execute(VarPool vp) {
                    if (ScriptManager.debugMode) {
                        Logger.error("Else statement is executed twice");
                    }
                    return true;
                }

            };
        }
    ),
    REF(
        Keyword.VAR + " (.+ *)=( *.+)",
        "(.+)(" + Keyword.FOR + "|" + Keyword.IF + "|" + Keyword.WHILE + ")(.+)",
        tn -> tn.line.contains("="),
        tn -> PipeRef.resolve(tn.line)
    ),
    ROOT(
        null, null,
        tn -> false,
        tn -> tn.parent == null ? new PipeRoot() : null
    ),
    WHILE(
        "(" + Keyword.WHILE + " ?)\\(.+\\)*", null,
        tn -> tn.line.startsWith(Keyword.WHILE.toString()),
        tn -> {
            if (tn.children.isEmpty()) {
                String[] ols = Expressions.oneLineStatement(tn.line);
                tn.firstScopeMember().write(ols[1]);
                tn.defineNodeTypes_r();
                return new PipeWhile(Expressions.resolveVar(ols[0]));
            } else {
                String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
                return new PipeWhile(Expressions.resolveVar(exp));
            }
        }
    ),
    XCS(
        null, "(.+)(" + Keyword.FOR + "|" + Keyword.IF + "|" + Keyword.WHILE + "|" + Keyword.EVENT + ")(.+)",
        tn -> tn.line.contains(".") &&
             !tn.line.contains("=") &&
              tn.line.contains("(") &&
              tn.line.contains(")"),
        tn -> new PipeXcs(Expressions.resolveVar(tn.line))
    ),
    OTHER( // Needs to be in the end
        null, null,
        tn -> true,
        tn -> new PipeXcs(Expressions.resolveVar(tn.line))
    );

    static SyntaxTreeNodeType of(SyntaxTreeNode tn) {
        for (SyntaxTreeNodeType type : values()) {
            if (type.test(tn)) {
                return type;
            }
        }
        return OTHER;
    }

    private final Pattern regEx, notRegEx;
    private final Predicate<SyntaxTreeNode> pred;
    private final SyntaxTreeCompiler comp;

    SyntaxTreeNodeType(
        String regEx,
        String notRegEx,
        Predicate<SyntaxTreeNode> pred,
        SyntaxTreeCompiler comp
    ) {
        this.regEx    = regEx    == null ? null : Pattern.compile(regEx);
        this.notRegEx = notRegEx == null ? null : Pattern.compile(notRegEx);
        this.pred = pred;
        this.comp = comp;
    }

    boolean test(SyntaxTreeNode node) {
        return pred.test(node)
                && (regEx != null ? regEx.matcher(node.line).matches() : true)
                && (notRegEx != null ? !notRegEx.matcher(node.line).matches() : true);
    }

    Pipe compile(SyntaxTreeNode treeNode, int lineIndex) {
        if (ScriptManager.debugMode) {
            Logger.info("Compiling " + treeNode.line + " [" + lineIndex + "] (" + treeNode.type.name() + ")");
        }
        Pipe pipe = comp.pass(treeNode);
        pipe.lineIndex = lineIndex;
        return pipe;
    }

}
