package com.github.inc0grepoz.kvad.ksf.exp;

import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.ksf.VarPool;
import com.github.inc0grepoz.kvad.ksf.var.Var;
import com.github.inc0grepoz.kvad.ksf.var.VarByValue;
import com.github.inc0grepoz.kvad.ksf.var.VarXcs;

public class ExpressionArguments {

    private static final String REGEX_OUTWARD_SPACES = "(^(\t| )+)|((\t| )+$)";
    private static final String REGEX_SEP_QUOTES = "(^(\")+)|((\")+$)";
    private static final String REGEX_COMMA = ",";
    private static final String REGEX_SEMICOLON = ";";

    public static Var[] resolveWithComma(VarPool varPool, String exp) {
        return resolve(varPool, exp, REGEX_COMMA);
    }

    public static Var[] resolveWithSemicolon(VarPool varPool, String exp) {
        return resolve(varPool, exp, REGEX_SEMICOLON);
    }

    private static Var[] resolve(VarPool varPool, String exp, String sep) {
        String localExp = exp.replaceAll(REGEX_OUTWARD_SPACES, "");

        // Removing brackets
        /*
        if (localExp.charAt(0) == '(') {
            localExp = localExp.substring(1, localExp.length() - 1);
        }
        if (localExp.charAt(localExp.length() - 1) == ')') {
            localExp = localExp.substring(0, localExp.length() - 2);
        }
        */

        char[] chars = exp.toCharArray();
        boolean quote = false;
        int brackets = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '.') {
                
            }
        }

        // TODO: no
        return Stream.of(localExp.split(sep)).map(str -> {
            if (Character.isDigit(str.charAt(0))) {
                return new VarByValue(toNumber(str));
            }

            if (str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
                return new VarByValue(str.replaceAll(REGEX_SEP_QUOTES, ""));
            }

            if (str.matches("(true|false)")) {
                return new VarByValue(Boolean.getBoolean(str));
            }

            if (!str.contains(".")) {
                return VarXcs.resolve(str);
            } else {
                
            }

            /*
            if (chars[i] == '"') {
                if (chars[i - 1] == '\\') { // Quote is not a special symbol
                    args += chars[i];
                    continue;
                } else if (!quote && chars[i - 1] == '"') { // Trailing quotes
                    args += chars[i];
                }
                quote = !quote;
            } else {
//                Variables.valueFromString(buffer.toString());
                args += chars[i];
            }
            */

            return new VarByValue(null);
        }).toArray(Var[]::new);
    }

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

}
