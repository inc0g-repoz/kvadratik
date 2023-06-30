package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

@FunctionalInterface
interface Evaluator {
    VarValue passOperands(VarPool varPool, Var... o);
}

public enum Operator {

    B_OR(
        "||",
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
    B_AND(
        "&&",
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
    B_EQ(
        "==",
        (vp, o) -> {
            Object v1 = o[0].getValue(vp), v2 = o[1].getValue(vp);
            if (v1 instanceof Number && v2 instanceof Number) {
                String d1s = String.valueOf(v1), d2s = String.valueOf(v2);
                return new VarValue(Double.valueOf(d1s).equals(Double.valueOf(d2s)));
            }
            return new VarValue(v1 == v2);
        },
        2
    ),
    B_GTR(
        ">",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) > Double.valueOf(d2s));
        },
        2
    ),
    B_LSS(
        "<",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) < Double.valueOf(d2s));
        },
        2
    ),
    B_EQ_N(
        "!=",
        (vp, o) -> new VarValue(!o[0].getValue(vp).equals(o[1].getValue(vp))),
        2
    ),
    B_EQ_GTR(
        ">=",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) >= Double.valueOf(d2s));
        },
        2
    ),
    B_EQ_LSS(
        "<=",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            return new VarValue(Double.valueOf(d1s) <= Double.valueOf(d2s));
        },
        2
    ),
    V_ASG(
        "=",
        (vp, o) -> {
            VarValue vv = (VarValue) o[0].getVar(vp);
            vv.value = o[1].getValue(vp);
            return vv;
        },
        2
    ),
    B_TERN(
        new String[] { "?", ":" },
        (vp, o) -> new VarValue((boolean) o[0].getValue(vp) ? o[1].getValue(vp) : o[2].getValue(vp)),
        3
    ),
    B_NOT(
        "!",
        (vp, o) -> {
            return new VarValue(!(boolean) o[0].getValue(vp));
        },
        1
    ),
    N_ADD(
        "+",
        (vp, o) -> {
            Object[] values = Stream.of(o).map(var -> var.getValue(vp)).toArray(Object[]::new);
            if (Stream.of(values).anyMatch(v -> v instanceof String)) {
                StringBuilder sb = new StringBuilder();
                for (Object obj : values) {
                    sb.append(String.valueOf(obj));
                }
                return new VarValue(sb.toString());
            } else {
                double r = 0;
                for (Object v : values) {
                    r += Double.valueOf(String.valueOf(v));
                }
                return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
            }
        },
        2
    ),
    N_SUB(
        "-",
        (vp, o) -> {
            String s = String.valueOf(o[0].getValue(vp));
            double r = Double.valueOf(s);
            for (int i = 1; i < o.length; i++) {
                s = String.valueOf(o[i].getValue(vp));
                r -= Double.valueOf(s);
            }
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        },
        2
    ),
    N_MULT(
        "*",
        (vp, o) -> {
            String s = String.valueOf(o[0].getValue(vp));
            double r = Double.valueOf(s);
            for (int i = 1; i < o.length; i++) {
                s = String.valueOf(o[i].getValue(vp));
                r *= Double.valueOf(s);
            }
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        },
        2
    ),
    N_DIV(
        "/",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) / Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        },
        2
    ),
    N_MOD(
        "%",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Double.valueOf(d1s) % Double.valueOf(d2s);
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        },
        2
    ),
    N_POW(
        "^",
        (vp, o) -> {
            String d1s = String.valueOf(o[0].getValue(vp));
            String d2s = String.valueOf(o[1].getValue(vp));
            double r = Math.pow(Double.valueOf(d1s), Double.valueOf(d2s));
            return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
        },
        2
    ),
    ;

    final String[] nttn;    // Notation
    final Evaluator eval; // Implementation
    final int oc;         // Operands count

    Operator(String nttn[], Evaluator eval, int operands) {
        this.nttn = nttn;
        this.eval = eval;
        this.oc = operands;
    }

    Operator(String nttn, Evaluator eval, int operands) {
        this.nttn = new String[] { nttn };
        this.eval = eval;
        this.oc = operands;
    }

    VarValue eval(VarPool varPool, Var... o) {
        return eval.passOperands(varPool, o);
    }

    VarOp resolve(String exp) {
        return oc > 1 ? resolveMultipleOperands(exp) : resolveOneOperand(exp);
    }

    private VarOp resolveMultipleOperands(String exp) {
        char[] chars = exp.toCharArray();
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

    private VarOp resolveOneOperand(String exp) {
        String ref = null;
        if (exp.startsWith(nttn[0])) {
            ref = exp.substring(nttn[0].length());
        } else if (exp.endsWith(nttn[0])) {
            ref = exp.substring(0, exp.length() - nttn[0].length());
        }
        return ref == null ? null : new VarOp(this, Expressions.resolveVar(ref));
    }

}
