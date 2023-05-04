package com.github.inc0grepoz.kvad.ksf;

import java.util.ArrayList;
import java.util.List;

public class Expressions {

    private static final String REGEX_OUTWARD_SPACES = "(^(\t| )+)|((\t| )+$)";
    private static final String REGEX_SEP_QUOTES = "(^(\")+)|((\")+$)";
//  private static final String REGEX_COMMA = ",";
//  private static final String REGEX_SEMICOLON = ";";

    public static Var resolveXcs(String argExp) {
        String exp = argExp.replaceAll(REGEX_OUTWARD_SPACES, "");

        if (exp.charAt(0) == '(' && exp.charAt(exp.length() - 1) == ')') {
            exp = exp.substring(1, exp.length() - 1);
            exp = exp.substring(0, exp.length() - 2);
        }

        if (Character.isDigit(exp.charAt(0))) {
            return toNumberVarValue(exp);
        }

        if (exp.charAt(0) == '\"' && exp.charAt(exp.length() - 1) == '\"') {
            return new VarValue(exp.replaceAll(REGEX_SEP_QUOTES, ""));
        }

        // TODO: Use !|(\|\|)|(&&) and parse boolean expressions
        if (exp.matches("true|false")) {
            return resolveBoolean(exp);
        }

        boolean quote = false, methodArgs = false;
        char[] chars = exp.toCharArray();
        int brackets = 0;

        StringBuilder name = new StringBuilder(), args = new StringBuilder();
        VarXcs firstXcs = null, lastXcs = null;

        for (int i = 0; i < chars.length; i++) {
            // Not reading any string values here
            if (!quote) {
                if (chars[i] == '.' || i == chars.length - 1) {
                    VarXcs nuXcs;
                    if (args.length() != 0) {
                        Var[] vars = resolveXcsBrackets(args.toString());
                        nuXcs = new VarXcsMethod(name.toString(), vars);
                        args.setLength(0);
                    } else {
                        nuXcs = new VarXcsField(name.toString());
                    }
                    name.setLength(0);

                    if (firstXcs == null) {
                        firstXcs = nuXcs;
                        lastXcs = nuXcs;
                    } else {
                        lastXcs.nextXcs = nuXcs;
                        lastXcs = nuXcs;
                    }
                    continue;
                }

                if (chars[i] == '(' && !methodArgs) {
                    if (brackets == 0) {
                        methodArgs = true;
                        continue; // 100% a method
                    }
                    brackets++;
                }

                if (chars[i] == ')' && methodArgs) {
                    brackets--;
                    if (brackets == 0) {
                        methodArgs = false;
                        continue; // Method invokation end
                    }
                }
            }

            // Writing characters into buffers
            if (methodArgs) {
                args.append(chars[i]);
            } else {
                name.append(chars[i]);
            }
        }

        return firstXcs;
    }

    public static Var[] resolveXcsBrackets(String args) {
        boolean quote = false;
        char[] chars = args.toCharArray();
        int brackets = 0, last = chars.length - 1;

        List<Var> elts = new ArrayList<>();
        StringBuilder arg = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            // Look, a quote!
            if (
                chars[i] == '\"'
                ||
                (i != 0 && chars[i - 1] == '\\' && chars[i] == '\"')
            ) {
                quote = !quote;
            }

            // No quote
            else if (!quote) switch (chars[i]) {
                case ' ':
                    continue;
                case '(':
                    brackets++;
                    break;
                case ')':
                    brackets--;
                    break;
            }

            if (brackets == 0 && !quote) {
                arg.append(chars[i]);
                if (chars[i] == ',' || i == last) {
                    elts.add(resolveXcs(arg.toString()));
                }
            }
        }

        return elts.stream().toArray(Var[]::new);
    }

    public static VarValue resolveBoolean(String exp) {
        return new VarValue(Boolean.getBoolean(exp));
    }

    private static VarValue toNumberVarValue(String string) {
        try {
            return new VarValue(Integer.parseInt(string));
        } catch (Throwable t) {}

        char lastChar = Character.toLowerCase(string.charAt(string.length() - 1));
        if (lastChar != 'd') try {
            return new VarValue(Float.parseFloat(string));
        } catch (Throwable t) {}

        try {
            return new VarValue(Double.parseDouble(string));
        } catch (Throwable t) {}

        throw new NumberFormatException("Invalid number " + string);
    }

}
