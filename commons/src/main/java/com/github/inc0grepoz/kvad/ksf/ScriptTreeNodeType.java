package com.github.inc0grepoz.kvad.ksf;

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
        tn -> new ScriptPipeOther()
    ),
    EVENT(
        "(" + Keyword.EVENT + " )(.+)\\(.+\\)*", null,
        tn -> tn.line.startsWith(Keyword.EVENT + " "),
        tn -> {
            String[] args = tn.line.split("(^" + Keyword.EVENT + " )|\\(|\\)");
            return new ScriptPipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        "(" + Keyword.FOR + " ?)\\(.+=.+;.+;.+\\)*", null,
        tn -> tn.line.startsWith(Keyword.FOR.toString()) && tn.line.contains("(") && tn.line.contains(")"),
        tn -> new ScriptPipeOther()
    ),
    IF(
        "(" + Keyword.IF + " ?)\\(.+\\)*", null,
        tn -> tn.line.startsWith(Keyword.IF.toString()) && tn.line.contains("(") && tn.line.contains(")"),
        tn -> {
            String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
            return new ScriptPipeConditional(Expressions.resolveVar(exp));
        }
    ),
    REF(
        Keyword.VAR + " (.+ ?)=( ?.+)",
        "(.+)(" + Keyword.FOR + "|" + Keyword.IF + "|" + Keyword.WHILE + ")(.+)",
        tn -> tn.line.contains("="),
        tn -> {
            String[] leftRight = tn.line.split(" ?= ?", 2);

            boolean newVar = leftRight[0].startsWith("var ");
            String name = newVar ? leftRight[0].substring(4) : leftRight[0];

            return new ScriptPipeRef(Expressions.resolveVar(leftRight[1]), name, newVar);
        }
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
            String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
            return new ScriptPipeWhile(Expressions.resolveVar(exp));
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
    private Compiler comp;

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

    ScriptPipe compile(ScriptTreeNode treeNode) {
        Logger.info("Compiling " + treeNode.line + " [" + treeNode.type.name() + "]");
        return comp.pass(treeNode);
    }

}
