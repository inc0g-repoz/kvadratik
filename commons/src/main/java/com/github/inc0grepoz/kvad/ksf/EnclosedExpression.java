package com.github.inc0grepoz.kvad.ksf;

import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.ksf.var.Var;
import com.github.inc0grepoz.kvad.ksf.var.VarByValue;
import com.github.inc0grepoz.kvad.ksf.var.VarXcs;

public class EnclosedExpression {

    private static final String REGEX_OUTWARD_SPACES = "(^(\t| )+)|((\t| )+$)";
    private static final String REGEX_SEP_QUOTES = "(^(\")+)|((\")+$)";
    private static final String REGEX_COMMA = ",";
    private static final String REGEX_SEMICOLON = ";";

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

    Var[] resolveWithComma(VarPool varPool, String exp) {
        return resolve(varPool, exp, REGEX_COMMA);
    }

    Var[] resolveWithSemicolon(VarPool varPool, String exp) {
        return resolve(varPool, exp, REGEX_SEMICOLON);
    }

    private Var[] resolve(VarPool varPool, String exp, String sep) {
        String localExp = exp.replaceAll(REGEX_OUTWARD_SPACES, "");

        // Removing brackets
        if (localExp.charAt(0) == '(') {
            localExp = localExp.substring(1, localExp.length() - 1);
        }
        if (localExp.charAt(localExp.length() - 1) == ')') {
            localExp = localExp.substring(0, localExp.length() - 2);
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

            return new VarByValue(null);
        }).toArray(Var[]::new);
    }

}
