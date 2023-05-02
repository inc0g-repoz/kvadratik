package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.utils.Logger;

@FunctionalInterface
interface Compiler {
    ScriptPipe pass(ScriptTreeNode tn);
}

public enum ScriptTreeNodeType {

    /* 
     * tn – tree node
     * vp – varpool
     */
    COMMENT(
        tn -> tn.line.startsWith("//"),
        tn -> new ScriptPipeMissing()
    ),
    EVENT(
        tn -> tn.line.startsWith("on ")
           && tn.line.matches("(on )(.+)\\(.+\\)*"),
        tn -> {
            String[] args = tn.line.split("(^on )|\\(|\\)");
            return new ScriptPipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        tn -> tn.line.startsWith("for") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(for ?)\\(.+=.+;.+;.+\\)*"),
        tn -> new ScriptPipeMissing()
    ),
    IF(
        tn -> tn.line.startsWith("if") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(if ?)\\(.+\\)*"),
        tn -> {
            String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
            return new ScriptPipeConditional(Expressions.resolveXcs(exp));
        }
    ),
    REF(
        tn -> tn.line.contains("=")
           && tn.line.matches("(var )?(.+ ?)=( ?.+)")
           && !tn.line.matches("(.+)(for|if|while)(.+)"),
        tn -> {
            String[] leftRight = tn.line.split(" ?= ?");

            boolean newVar = leftRight[0].startsWith("var ");
            String name = newVar ? leftRight[0].substring(4) : leftRight[0];

            return new ScriptPipeRef(Expressions.resolveXcs(leftRight[1]), name, newVar);
        }
    ),
    ROOT(
        tn -> false,
        tn -> tn.parent == null ? new ScriptPipeRoot() : null
    ),
    WHILE(
        tn -> tn.line.startsWith("while")
           && tn.line.matches("(while ?)\\(.+\\)*"),
        tn -> {
            String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
            return new ScriptPipeWhile(Expressions.resolveXcs(exp));
        }
    ),
    XCS(
        tn -> tn.line.contains(".") && !tn.line.contains("=")
           && tn.line.contains("(") && tn.line.contains(")")
           && !tn.line.matches("(.+)(for|if|while|on)(.+)"),
        tn -> new ScriptPipeXcs(Expressions.resolveXcs(tn.line))
    ),
    OTHER( // Needs to be in the end
        tn -> true,
        tn -> new ScriptPipeMissing()
    );

    private final Predicate<ScriptTreeNode> pred;
    private Compiler comp;

    ScriptTreeNodeType(
        Predicate<ScriptTreeNode> pred,
        Compiler comp
    ) {
        this.pred = pred;
        this.comp = comp;
    }

    boolean test(ScriptTreeNode node) {
        return pred.test(node);
    }

    ScriptPipe compile(ScriptTreeNode treeNode) {
        Logger.info("Compiling " + treeNode.line + " [" + treeNode.type.name() + "]");
        return comp.pass(treeNode);
    }

}
