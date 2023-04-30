package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.ksf.exp.ExpressionAccess;

@FunctionalInterface
interface Compiler {
    ScriptPipe pass(Script script, ScriptTreeNode tn, VarPool varPool);
}

public enum ScriptTreeNodeType {

    /* s  – script
     * tn – tree node
     * vp – varpool
     */
    COMMENT(
        tn -> tn.line.startsWith("//"),
        (s, tn, vp) -> null
    ),
    FOR(
        tn -> tn.line.startsWith("for") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(for ?)\\(.+=.+;.+;.+\\)*"),
        (s, tn, vp) -> null
    ),
    FUN(
        tn -> tn.line.startsWith("on ")
           && tn.line.matches("(on )(.+)\\(.+\\)*"),
        (s, tn, vp) -> null
    ),
    IF(
        tn -> tn.line.startsWith("if") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(if ?)\\(.+\\)*"),
        (s, tn, vp) -> null
    ),
    REF(
        tn -> tn.line.contains("=")
           && tn.line.matches("(var )?(.+ ?)=( ?.+)")
           && !tn.line.matches("(.+)(for|if|while)(.+)"),
        (s, tn, vp) -> null
    ),
    VOID(
        tn -> tn.line.contains(".") && !tn.line.contains("=")
           && tn.line.contains("(") && tn.line.contains(")")
           && !tn.line.matches("(.+)(for|if|while|on)(.+)"),
        (s, tn, vp) -> {
            ScriptPipeVoid cn = new ScriptPipeVoid();
            cn.var = ExpressionAccess.resolve(s.global, tn.line);
            return cn;
        }
    ),
    WHILE(
        tn -> tn.line.startsWith("while")
           && tn.line.matches("(while ?)\\(.+\\)*"),
        (s, tn, vp) -> null
    ),
    OTHER( // Needs to be in the end
        tn -> true,
        (s, tn, vp) -> null
    );

    private final Predicate<ScriptTreeNode> pred;
    private Compiler comp;

    ScriptTreeNodeType(
        Predicate<ScriptTreeNode> pred,
        Compiler comp
    ) {
        this.pred = pred;
    }

    boolean test(ScriptTreeNode node) {
        return pred.test(node);
    }

    ScriptPipe compile(Script script, ScriptTreeNode treeNode, VarPool varPool) {
        return comp.pass(script, treeNode, varPool);
    }

}
