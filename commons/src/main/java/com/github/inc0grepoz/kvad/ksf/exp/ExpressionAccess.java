package com.github.inc0grepoz.kvad.ksf.exp;

import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.kvad.ksf.VarPool;
import com.github.inc0grepoz.kvad.ksf.var.Var;
import com.github.inc0grepoz.kvad.ksf.var.VarByValue;
import com.github.inc0grepoz.kvad.ksf.var.VarXcs;
import com.github.inc0grepoz.kvad.ksf.var.VarXcsField;
import com.github.inc0grepoz.kvad.ksf.var.VarXcsMethod;

public class ExpressionAccess {

    private static final String REGEX_OUTWARD_SPACES = "(^(\t| )+)|((\t| )+$)";
    private static final String REGEX_SEP_QUOTES = "(^(\")+)|((\")+$)";
//  private static final String REGEX_COMMA = ",";
//  private static final String REGEX_SEMICOLON = ";";

    private static Var toNumber(String string) {
        try {
            return new VarByValue(Integer.parseInt(string));
        } catch (Throwable t) {}

        char lastChar = Character.toLowerCase(string.charAt(string.length() - 1));
        if (lastChar != 'd') try {
            return new VarByValue(Float.parseFloat(string));
        } catch (Throwable t) {}

        try {
            return new VarByValue(Double.parseDouble(string));
        } catch (Throwable t) {}

        throw new NumberFormatException("Invalid number " + string);
    }

    public static Var resolve(VarPool varPool, String argExp) {
        String exp = argExp.replaceAll(REGEX_OUTWARD_SPACES, "");

        if (exp.charAt(0) == '(') {
            exp = exp.substring(1, exp.length() - 1);
        }

        if (exp.charAt(exp.length() - 1) == ')') {
            exp = exp.substring(0, exp.length() - 2);
        }

        if (Character.isDigit(exp.charAt(0))) {
            return new VarByValue(toNumber(exp));
        }

        if (exp.charAt(0) == '\"' && exp.charAt(exp.length() - 1) == '\"') {
            return new VarByValue(exp.replaceAll(REGEX_SEP_QUOTES, ""));
        }

        if (exp.matches("(true|false)")) {
            return new VarByValue(Boolean.getBoolean(exp));
        }

        boolean quote = false, methodArgs = false;
        char[] chars = exp.toCharArray();
        int brackets = 0;

        String name = new String(), args = new String();
        VarXcs varXcs = null;

        for (int i = 0; i < chars.length; i++) {
            if (
                !quote && (chars[i] == '.' || i == chars.length - 1)
            ) {
                VarXcs nuXcs;
                if (methodArgs) {
                    Var[] vars = resolveBrackets(varPool, args);
                    nuXcs = new VarXcsMethod(varPool, name, vars);
                    args = new String();
                } else {
                    nuXcs = new VarXcsField(varPool, name);
                }
                name = new String();

                if (varXcs == null) {
                    varXcs = nuXcs;
                } else {
                    varXcs = varXcs.nextXcs = nuXcs;
                }
                continue;
            }

            if (chars[i] == '(' && !quote && !methodArgs) {
                if (brackets == 0) {
                    methodArgs = true;
                    continue; // 100% a method
                }
                brackets++;
            }

            if (chars[i] == ')' && !quote && methodArgs) {
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

        return null;
    }

    public static Var[] resolveBrackets(VarPool varPool, String args) {
        boolean quote = false;
        char[] chars = args.toCharArray();
        int brackets = 0, last = chars.length - 1;

        List<Var> elts = new ArrayList<>();
        String arg = new String();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && !quote) {
                brackets++;
            }

            if (chars[i] == ')' && !quote) {
                brackets--;
            }

            if (
                chars[i] == '\"'
                    || (i != 0 && chars[i - 1] == '\\')
            ) {
                quote = !quote;
            }

            if (brackets == 0 && !quote) {
                if (chars[i] == ',' || i == last) {
                    elts.add(resolve(varPool, arg));
                } else {
                    arg += chars[i];
                }
            }
        }

        return elts.stream().toArray(Var[]::new);
    }

}
