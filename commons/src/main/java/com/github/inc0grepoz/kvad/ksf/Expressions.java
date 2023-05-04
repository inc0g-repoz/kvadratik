package com.github.inc0grepoz.kvad.ksf;

import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.kvad.utils.Logger;

public class Expressions {

    private static final String REGEX_OUTWARD_SPACES = "(^(\t| )+)|((\t| )+$)";
    private static final String REGEX_SEP_QUOTES = "(^(\")+)|((\")+$)";
//  private static final String REGEX_COMMA = ",";
//  private static final String REGEX_SEMICOLON = ";";

    public static Var resolveVar(String argExp) {
        String exp = argExp.replaceAll(REGEX_OUTWARD_SPACES, "");

        while (exp.charAt(0) == '(' && exp.charAt(exp.length() - 1) == ')') {
            exp = exp.substring(1, exp.length() - 1);
        }

        if (Character.isDigit(exp.charAt(0))) {
            return toNumberVarValue(exp);
        }

        if (exp.charAt(0) == '"' && exp.charAt(exp.length() - 1) == '"') {
            return new VarValue(exp.replaceAll(REGEX_SEP_QUOTES, ""));
        }

        // TODO: Use !|(\|\|)|(&&) and parse boolean expressions
        if (exp.matches("true|false")) {
            return resolveBoolean(exp);
        }

        return resolveXcs(exp);
    }

    private static VarXcs resolveXcs(String exp) {
        boolean quote = false, methodArgs = false;
        char[] chars = exp.toCharArray();
        int brackets = 0;

        StringBuilder name = new StringBuilder(), args = new StringBuilder();
        VarXcs firstXcs = null, lastXcs = null;

        for (int i = 0; i < chars.length; i++) {
            write: {
                if (chars[i] == '"' && !(i != 0 && chars[i - 1] == '\\')) {
                    quote = !quote;
                }

                // Character is not in a string
                else if (!quote) {
                    if (chars[i] == '(') {
                        brackets++;
                        if (brackets == 1) {
                            methodArgs = true; // 100% a method
                            break write;
                        }
                    }
                    if (chars[i] == ')') {
                        brackets--;
                        if (brackets == 0) {
                            // Method args end found
                            break write;
                        }
                    }
                    if (chars[i] == '.') {
                        break write;
                    }
                }

                // Writing characters into buffers
                if (brackets != 0) {
                    args.append(chars[i]);
                } else {
                    name.append(chars[i]);
                }
            }

            if (
                brackets == 0 // Brackets are closed
                &&
                (chars[i] == '.' || i == chars.length - 1) // Separator or end of line found
            ) {
                VarXcs nuXcs;
                if (methodArgs) {
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

                methodArgs = false;
                continue;
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
            arg.append(chars[i]);

            // Look, a quote!
            if (chars[i] == '"' && !(i != 0 && chars[i - 1] == '\\')) {
                quote = !quote;
            }

            // No quote
            if (!quote) {
                switch (chars[i]) {
                    case '(': {
                        brackets++;
                        break;
                    }
                    case ')': {
                        brackets--;
                        break;
                    }
                    case ' ': continue;
                }

                if (!quote && brackets == 0 && (chars[i] == ',' || i == last)) {
                    elts.add(resolveVar(arg.toString()));
                    arg.setLength(0);
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
