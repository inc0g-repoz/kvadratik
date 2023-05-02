package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.utils.Logger;

@FunctionalInterface
interface Compiler {
    ScriptPipe pass(ScriptTreeNode tn, VarPool varPool);
}

public enum ScriptTreeNodeType {

    /* 
     * tn – tree node
     * vp – varpool
     */
    COMMENT(
        tn -> tn.line.startsWith("//"),
        (tn, vp) -> new ScriptPipeMissing()
    ),
    EVENT(
        tn -> tn.line.startsWith("on ")
           && tn.line.matches("(on )(.+)\\(.+\\)*"),
        (tn, vp) -> {
            String[] args = tn.line.split("(^on )|\\(|\\)");
            return new ScriptPipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        tn -> tn.line.startsWith("for") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(for ?)\\(.+=.+;.+;.+\\)*"),
        (tn, vp) -> new ScriptPipeMissing()
    ),
    IF(
        tn -> tn.line.startsWith("if") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(if ?)\\(.+\\)*"),
        (tn, vp) -> {
            String exp = tn.line.substring(tn.line.indexOf('(') + 1, tn.line.lastIndexOf(')'));
            return new ScriptPipeIf(Expressions.resolveXcs(exp));
        }
    ),
    REF(
        tn -> tn.line.contains("=")
           && tn.line.matches("(var )?(.+ ?)=( ?.+)")
           && !tn.line.matches("(.+)(for|if|while)(.+)"),
        (tn, vp) -> {
            String[] leftRight = tn.line.split(" ?= ?");

            boolean newVar = leftRight[0].startsWith("var ");
            String name = newVar ? leftRight[0].substring(4) : leftRight[0];

            return new ScriptPipeRef(Expressions.resolveXcs(leftRight[1]), name, newVar);
        }
    ),
    ROOT(
        tn -> false,
        (tn, vp) -> tn.parent == null ? new ScriptPipeRoot() : null
    ),
    WHILE(
        tn -> tn.line.startsWith("while")
           && tn.line.matches("(while ?)\\(.+\\)*"),
        (tn, vp) -> new ScriptPipeMissing()
    ),
    XCS(
        tn -> tn.line.contains(".") && !tn.line.contains("=")
           && tn.line.contains("(") && tn.line.contains(")")
           && !tn.line.matches("(.+)(for|if|while|on)(.+)"),
        (tn, vp) -> new ScriptPipeXcs(Expressions.resolveXcs(tn.line))
    ),
    OTHER( // Needs to be in the end
        tn -> true,
        (tn, vp) -> new ScriptPipeMissing()
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

    ScriptPipe compile(ScriptTreeNode treeNode, VarPool varPool) {
        Logger.info("Compiling " + treeNode.line + " [" + treeNode.type.name() + "]");
        return comp.pass(treeNode, varPool);
    }

}
