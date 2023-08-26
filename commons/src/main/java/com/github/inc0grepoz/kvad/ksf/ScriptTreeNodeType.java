package com.github.inc0grepoz.kvad.ksf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.utils.Logger;

@FunctionalInterface
interface Compiler {
    ScriptPipe pass(ScriptTreeNode tn);
}

public enum ScriptTreeNodeType {

    COMMENT(
        null, null,
        tn -> tn.line.startsWith("//"),
        tn -> new ScriptPipeOther() {

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
            return new ScriptPipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        "(" + Keyword.FOR + " ?)\\((.+=.+)?;.*;.*\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.FOR.toString()) && tn.line.contains("(") && tn.line.contains(")") && tn.line.contains(";"),
        tn -> {
            String[] ols = Expressions.oneLineStatement(tn.line);
            String[] params = ols[0].split(";");

            ScriptPipeRef ref = ScriptPipeRef.resolve(params[0]);
            Var boolExp = Expressions.resolveVar(params[1]);
            Var op = Expressions.resolveVar(params[2]);

            if (tn.children.isEmpty()) {
                tn.firstScopeMember().write(ols[1]);
                tn.defineTypes_r();
            }
            return new ScriptPipeFor(ref, boolExp, op);
        }
    ),
    FOR_EACH(
        "(" + Keyword.FOR + " ?)\\(.+:.+\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.FOR.toString()) && tn.line.contains("(") && tn.line.contains(")") && tn.line.contains(":"),
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
                tn.defineTypes_r();
            }
            return new ScriptPipeForEach(typeParam.get(1), Expressions.resolveVar(params[1]));
        }
    ),
    IF(
        "(" + Keyword.IF + " ?)\\(.+\\)(.*)", null,
        tn -> tn.line.startsWith(Keyword.IF.toString()) && tn.line.contains("(") && tn.line.contains(")"),
        tn -> {
            if (tn.children.isEmpty()) {
                String[] ols = Expressions.oneLineStatement(tn.line);
                tn.firstScopeMember().write(ols[1]);
                tn.defineTypes_r();
                return new ScriptPipeIf(Expressions.resolveVar(ols[0]));
            } else {
                String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
                return new ScriptPipeIf(Expressions.resolveVar(exp));
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
                tn.defineTypes_r();
            }
            ScriptPipeIf ifSt = (ScriptPipeIf) tn.prev.compiled;
            return ifSt.elsePipe = new ScriptPipeOther() {

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
        tn -> ScriptPipeRef.resolve(tn.line)
    ),
    ROOT(
        null, null,
        tn -> false,
        tn -> tn.parent == null ? new ScriptPipeRoot() : null
    ),
    WHILE(
        "(" + Keyword.WHILE + " ?)\\(.+\\)*", null,
        tn -> tn.line.startsWith(Keyword.WHILE.toString()),
        tn -> {
            if (tn.children.isEmpty()) {
                String[] ols = Expressions.oneLineStatement(tn.line);
                tn.firstScopeMember().write(ols[1]);
                tn.defineTypes_r();
                return new ScriptPipeWhile(Expressions.resolveVar(ols[0]));
            } else {
                String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
                return new ScriptPipeWhile(Expressions.resolveVar(exp));
            }
        }
    ),
    XCS(
        null, "(.+)(" + Keyword.FOR + "|" + Keyword.IF + "|" + Keyword.WHILE + "|" + Keyword.EVENT + ")(.+)",
        tn -> tn.line.contains(".") && !tn.line.contains("=")
           && tn.line.contains("(") && tn.line.contains(")"),
        tn -> new ScriptPipeXcs(Expressions.resolveVar(tn.line))
    ),
    OTHER( // Needs to be in the end
        null, null,
        tn -> true,
        tn -> new ScriptPipeXcs(Expressions.resolveVar(tn.line))
    );

    static ScriptTreeNodeType of(ScriptTreeNode tn) {
        for (ScriptTreeNodeType type : values()) {
            if (type.test(tn)) {
                return type;
            }
        }
        return OTHER;
    }

    private final String regEx, notRegEx;
    private final Predicate<ScriptTreeNode> pred;
    private final Compiler comp;

    ScriptTreeNodeType(
        String regEx,
        String notRegEx,
        Predicate<ScriptTreeNode> pred,
        Compiler comp
    ) {
        this.regEx = regEx;
        this.notRegEx = notRegEx;
        this.pred = pred;
        this.comp = comp;
    }

    boolean test(ScriptTreeNode node) {
        return pred.test(node) && (regEx != null ? node.line.matches(regEx) : true)
                && (notRegEx != null ? !node.line.matches(notRegEx) : true);
    }

    ScriptPipe compile(ScriptTreeNode treeNode, int lineIndex) {
        if (ScriptManager.debugMode) {
            Logger.info("Compiling " + treeNode.line + " [" + lineIndex + "] (" + treeNode.type.name() + ")");
        }
        ScriptPipe pipe = comp.pass(treeNode);
        pipe.lineIndex = lineIndex;
        return pipe;
    }

}
