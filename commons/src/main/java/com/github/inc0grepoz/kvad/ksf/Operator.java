package com.github.inc0grepoz.kvad.ksf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
interface Evaluator {
    VarValue passOperands(VarPool varPool, Var... o);
}

public enum Operator {

    // TODO: Resolve several access expressions as operands seperated by arithmetic and boolean operators:
    // 1. search for expressions enclosed in brackets from deepest to the very last outer ones;
    // 2. resolve operators using regular expressions in a prioritized order.
    // 3. brackets operator

    N_ADD(
        "+",
        "(\\w*)\\+(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) + Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_SUB(
        "-",
        "(\\w*)-(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) - Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MULT(
        "*",
        "(\\w)\\*(\\w)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) * Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_DIV(
        "/",
        "(\\w*)\\/(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) / Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    N_MOD(
        "%",
        "(\\w*)%(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) % Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (int) r : r);
        },
        2
    ),
    B_NOT(
        "!",
        "!(\\w*)",
        (vp, o) -> new VarValue(!(boolean) o[1].getValue(vp)),
        1
    ),
    B_AND(
        "&&",
        "(\\w*)&&(\\w*)",
        (vp, o) -> {
            for (Var v : o) {
                if (!(boolean) v.getValue(vp)) {
                    return new VarValue(false);
                }
            }
            return new VarValue(true);
        },
        2
    ),
    B_OR(
        "||",
        "(\\w*)\\|\\|(\\w*)",
        (vp, o) -> {
            for (Var v : o) {
                if ((boolean) v.getValue(vp)) {
                    return new VarValue(true);
                }
            }
            return new VarValue(false);
        },
        2
    ),
    B_GTR(
        ">",
        "(\\w*)>(\\w*)",
        (vp, o) -> new VarValue((double) o[1].getValue(vp) > (double) o[2].getValue(vp)),
        2
    ),
    B_LSS(
        "<",
        "(\\w*)<(\\w*)",
        (vp, o) -> new VarValue((double) o[1].getValue(vp) < (double) o[2].getValue(vp)),
        2
    ),
    B_EQ(
        "==",
        "(\\w*)==(\\w*)",
        (vp, o) -> new VarValue(o[0].getValue(vp).equals(o[1].getValue(vp))),
        2
    ),
    B_EQ_N(
        "!=",
        "(\\w*)!=(\\w*)",
        (vp, o) -> new VarValue(!o[0].getValue(vp).equals(o[1].getValue(vp))),
        2
    ),
    B_EQ_GTR(
        ">=",
        "(\\w*)>=(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) >= Double.valueOf(d2s));
        },
        2
    ),
    B_EQ_LSS(
        "<=",
        "(\\w*)<=(\\w*)",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) <= Double.valueOf(d2s));
        },
        2
    ),
    B_TERN(
        new String[] { "?", ":" },
        "(\\w*)\\?(\\w*):(\\w*)",
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) ? o[1].getValue(vp) : o[2].getValue(vp)),
        3
    ),
    V_ASG(
        "=",
        "(\\w*)=(\\w*)",
        (vp, o) -> {
            VarValue vv = (VarValue) o[0];
            vv.value = o[1].getValue(vp);
            return vv;
        },
        2
    );

    final static Operator[] REVERSED;

    static {
        List<Operator> list = Stream.of(values()).collect(Collectors.toList());
        Collections.reverse(list);
        REVERSED = list.stream().toArray(Operator[]::new);
    }

    final String[] nttn;    // Notation
    final Pattern syntax; // Syntax template
//  final Resolver reslv; // Syntax resolver
    final Evaluator eval; // Implementation
    final int oc;         // Operands count

    Operator(String nttn[], String syntax, Evaluator eval, int operands) {
        this.nttn = nttn;
        this.syntax = Pattern.compile(syntax);
        this.eval = eval;
        this.oc = operands;
    }

    Operator(String nttn, String syntax, Evaluator eval, int operands) {
        this.nttn = new String[] { nttn };
        this.syntax = Pattern.compile(syntax);
        this.eval = eval;
        this.oc = operands;
    }

    VarOp resolve(String codeLine) {
        char[] chars = codeLine.toCharArray();
        int lastOperandIdx = chars.length - 1, lastOperatorIdx = nttn.length - 1, brackets = 0;

        Queue<String> operands = new LinkedList<>();
        String lastOperand = new String();
        String nextOperator;

        for (int i = 0; i < chars.length; i++) {

            // Operand may include brackets
            if (chars[i] == '(') {
                brackets++;
            } else if (chars[i] == ')') {
                brackets--;
            }

            lastOperand += chars[i];
            nextOperator = operands.size() < nttn.length ? nttn[operands.size()] : nttn[lastOperatorIdx];

            if (brackets == 0) {
                if (lastOperand.endsWith(nextOperator)) {
                    operands.add(lastOperand = lastOperand.substring(0, lastOperand.length() - nextOperator.length()));
                    lastOperand = new String();
                } else if (i == lastOperandIdx) {
                    operands.add(lastOperand);
                }
            }
        }

        return operands.size() <= 1 ? null : new VarOp(this, operands.stream()
                .map(Expressions::resolveVar).toArray(Var[]::new));
    }

    VarValue eval(VarPool varPool, Var... o) {
        return eval.passOperands(varPool, o);
    }

}
