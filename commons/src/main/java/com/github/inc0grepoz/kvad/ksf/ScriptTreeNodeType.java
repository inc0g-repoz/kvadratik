package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.ksf.exp.ExpressionAccess;
import com.github.inc0grepoz.kvad.utils.Logger;

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
        (s, tn, vp) -> new ScriptPipeMissing()
    ),
    EVENT(
        tn -> tn.line.startsWith("on ")
           && tn.line.matches("(on )(.+)\\(.+\\)*"),
        (s, tn, vp) -> {
            String[] args = tn.line.split("(^on )|\\(|\\)");
            return new ScriptPipeEvent(args[1], args[2]);
        }
    ),
    FOR(
        tn -> tn.line.startsWith("for") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(for ?)\\(.+=.+;.+;.+\\)*"),
        (s, tn, vp) -> new ScriptPipeMissing()
    ),
    IF(
        tn -> tn.line.startsWith("if") && tn.line.contains("(") && tn.line.contains(")")
           && tn.line.matches("(if ?)\\(.+\\)*"),
        (s, tn, vp) -> new ScriptPipeMissing()
    ),
    REF(
        tn -> tn.line.contains("=")
           && tn.line.matches("(var )?(.+ ?)=( ?.+)")
           && !tn.line.matches("(.+)(for|if|while)(.+)"),
        (s, tn, vp) -> new ScriptPipeMissing()
    ),
    ROOT(
        tn -> false,
        (s, tn, vp) -> tn.parent == null ? new ScriptPipeRoot() : null
    ),
    VOID(
        tn -> tn.line.contains(".") && !tn.line.contains("=")
           && tn.line.contains("(") && tn.line.contains(")")
           && !tn.line.matches("(.+)(for|if|while|on)(.+)"),
        (s, tn, vp) -> {
            ScriptPipeVoid pipe = new ScriptPipeVoid();
            pipe.var = ExpressionAccess.resolve(s.global, tn.line);
            return pipe;
        }
    ),
    WHILE(
        tn -> tn.line.startsWith("while")
           && tn.line.matches("(while ?)\\(.+\\)*"),
        (s, tn, vp) -> new ScriptPipeMissing()
    ),
    OTHER( // Needs to be in the end
        tn -> true,
        (s, tn, vp) -> new ScriptPipeMissing()
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

    ScriptPipe compile(Script script, ScriptTreeNode treeNode, VarPool varPool) {
        Logger.info("Compiling " + treeNode.line + " [" + treeNode.type.name() + "]");
        return comp.pass(script, treeNode, varPool);
    }

}
