package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Predicate;

import com.github.inc0grepoz.kvad.ksf.exp.ExpressionArguments;
import com.github.inc0grepoz.kvad.ksf.var.Var;
import com.github.inc0grepoz.kvad.ksf.var.VarXcs;
import com.github.inc0grepoz.kvad.ksf.var.VarXcsField;
import com.github.inc0grepoz.kvad.ksf.var.VarXcsMethod;

@FunctionalInterface
interface Compiler {
    ScriptCompNode pass(Script script, ScriptTreeNode tn, VarPool varPool);
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
            ScriptCompNodeVoid cn = new ScriptCompNodeVoid();

            boolean quote = false, methodArgs = false;
            char[] chars = tn.line.toCharArray();
            int brackets = 0;

            String name = new String(), args = new String();
            VarXcs varXcs = null;

            for (int i = 0; i < chars.length; i++) {
                if (
                    !quote && brackets == 0 &&
                    (chars[i] == '.' || chars[i] == ';')
                ) {
                    VarXcs nuXcs;
                    if (methodArgs) {
                        Var[] vars = ExpressionArguments.resolveWithComma(s.global, args);
                        nuXcs = new VarXcsMethod(vp, name, vars);
                        args = new String();
                    } else {
                        nuXcs = new VarXcsField(vp, name);
                    }
                    name = new String();

                    if (varXcs == null) {
                        varXcs = nuXcs;
                    } else {
                        varXcs = varXcs.nextXcs = nuXcs;
                    }
                    continue;
                }

                if (chars[i] == '(' && !quote) {
                    if (brackets == 0) {
                        methodArgs = true;
                        continue; // 100% a method
                    }
                    brackets++;
                }

                if (chars[i] == ')' && !quote) {
                    brackets--;
                    if (brackets == 0) {
                        methodArgs = false;
                        continue; // Method invokation end
                    }
                }

                if (methodArgs) {
                    args += chars[i];
                } else {
                    name += chars[i];
                }
            }

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

    ScriptCompNode compile(Script script, ScriptTreeNode treeNode, VarPool varPool) {
        return comp.pass(script, treeNode, varPool);
    }

}
