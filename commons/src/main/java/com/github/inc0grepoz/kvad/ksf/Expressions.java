package com.github.inc0grepoz.kvad.ksf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Expressions {

    private static final Pattern REGEX_DIGITS = Pattern.compile("-?[0-9]+d?");

    static Var resolveVar(String argExp) {
        StringBuffer sb = new StringBuffer(argExp);

        while (sb.charAt(0) == ' ') {
            sb.deleteCharAt(0);
        }

        int last = sb.length() - 1;
        while (sb.charAt(last) == ' ') {
            sb.deleteCharAt(last);
            last--;
        }

        brackets: while (sb.charAt(0) == '(' && sb.charAt(last) == ')') {
            int allClosed = 0, brackets = 0;

            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '(') {
                    if (allClosed != 0) {
                        break brackets;
                    }

                    brackets++;
                } else if (sb.charAt(i) == ')') {
                    brackets--;

                    if (brackets == 0) {
                        allClosed++;
                    }
                }
            }

            sb.deleteCharAt(last);
            sb.deleteCharAt(0);
            last -= 2;
        }

        String exp = sb.toString();

        if (REGEX_DIGITS.matcher(exp).matches()) {
            return toNumberVarValue(exp);
        }

        if (exp.equals("true") || exp.equals("false")) {
            return resolveBoolean(exp);
        }

        return resolveVarOp(exp);
    }

    private static Var resolveVarOp(String exp, Var... vars) {
        VarOp varOp = null;

        for (Operator enumOp : Operator.values()) {
            varOp = enumOp.resolve(exp);
            if (varOp != null) {
                return varOp;
            }
        }

        return resolveXcs(exp);
    }

    private static VarXcs resolveXcs(String exp) {
        boolean negate = false, methodArgs = false;
        char[] chars = exp.toCharArray();
        int brackets = 0;

        StringBuilder name = new StringBuilder(), args = new StringBuilder();
        VarXcs firstXcs = null, lastXcs = null;

        for (int i = 0; i < chars.length; i++) loop: {
            write: {
                if (chars[i] == '!' && brackets == 0) {
                    negate = true;
                    break loop;
                }
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
                nuXcs.negate = negate;

                // Assigning default values for the next iteration
                negate = false;
                methodArgs = false;
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
        }

        return firstXcs;
    }

    private static Var[] resolveXcsBrackets(String args) {
        char[] chars = args.toCharArray();
        int brackets = 0, last = chars.length - 1;

        List<Var> elts = new ArrayList<>();
        StringBuilder arg = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            arg.append(chars[i]);

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

            if (brackets == 0 && (chars[i] == ',' || i == last)) {
                elts.add(resolveVar(arg.toString()));
                arg.setLength(0);
            }
        }

        return elts.stream().toArray(Var[]::new);
    }

    private static VarValue resolveBoolean(String exp) {
        return new VarValue(Boolean.parseBoolean(exp));
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
