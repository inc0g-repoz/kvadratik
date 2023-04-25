package com.github.inc0grepoz.kvad.ksf;

import java.util.function.Function;
import java.util.function.Predicate;

public enum TreeNodeType {

    COMMENT(
        n -> n.line.startsWith("//"),
        n -> null
    ),
    FOR(
        n -> n.line.startsWith("for") && n.line.contains("(") && n.line.contains(")")
          && n.line.matches("(for ?)\\(.+=.+;.+;.+\\)*"),
        n -> null
    ),
    FUN(
        n -> n.line.startsWith("on ")
          && n.line.matches("(on )(.+)\\(.+\\)*"),
        n -> null
    ),
    IF(
        n -> n.line.startsWith("if") && n.line.contains("(") && n.line.contains(")")
          && n.line.matches("(if ?)\\(.+\\)*"),
        n -> null
    ),
    REF(
        n -> n.line.contains("=")
          && n.line.matches("(var )?(.+ ?)=( ?.+)")
          && !n.line.matches("(.+)(for|if|while)(.+)"),
        n -> null
    ),
    VOID(
        n -> n.line.contains(".") && !n.line.contains("=")
          && n.line.contains("(") && n.line.contains(")")
          && !n.line.matches("(.+)(for|if|while|on)(.+)"),
        n -> {
            CompNodeVoid comp = new CompNodeVoid();

            char[] chars = n.line.toCharArray();
            boolean brace = false, quote = false;

            Accessible xs = null;
            StringBuilder buffer = new StringBuilder();
            

            for (int i = 0; i < chars.length; i++) {
                if (!quote && chars[i] == '.') {
                    String name = buffer.toString();
                    Variables.valueFromString(name);
                    comp.xsibles.add(xs);
                    xs = Accessible.initLocalScopeAccessible(name);
                }

                // Braces for methods
                if (!brace && !quote && chars[i] == '(') {
                    if (xs == null) {
                        throw new RuntimeException(buffer + " cannot be accessed");
                    }

                    xs.methodInvokation = true; // 100% is a method
                    brace = true;
                } else if (brace && !quote && chars[i] == ')') {
                    brace = false;
                }

                // For string values
                if (!xs.methodInvokation && chars[i] == '.') {
                    
                }

                if (chars[i] == '"') {
                    if (chars[i - 1] == '\\') { // Quote is not a special symbol
                        buffer.append(chars[i]);
                        continue;
                    } else if (!quote && chars[i - 1] == '"') { // Trailing quotes
                        buffer.append(chars[i]);
                    }
                    quote = !quote;
                } else {
                    Variables.valueFromString(buffer.toString());
                    buffer.append(chars[i]);
                }
            }

            return comp;
        }
    ),
    WHILE(
        n -> n.line.startsWith("while")
          && n.line.matches("(while ?)\\(.+\\)*"),
        n -> null
    ),
    OTHER( // Needs to be in the end
        n -> true,
        n -> null
    );

    private final Predicate<TreeNode> pred;
    private Function<TreeNode, CompNode> comp;

    TreeNodeType(
        Predicate<TreeNode> pred,
        Function<TreeNode, CompNode> comp
    ) {
        this.pred = pred;
    }

    boolean test(TreeNode node) {
        return pred.test(node);
    }

    CompNode compile(TreeNode node) {
        return comp.apply(node);
    }

}
