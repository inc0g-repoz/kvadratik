package com.github.inc0grepoz.kvad.common.ksf;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public enum OperatorType {

    /* The order of the elements below determines
     * the operators priority for the compiler */
    B_OR    (OperatorBoolOr::new,    2, "||"),
    B_AND   (OperatorBoolAnd::new,   2, "&&"),
    B_EQ    (OperatorBoolEq::new,    2, "=="),
    B_EQ_GTR(OperatorBoolEqGtr::new, 2, ">="),
    B_EQ_LSS(OperatorBoolEqLss::new, 2, "<="),
    B_GTR   (OperatorBoolGtr::new,   2, ">"),
    B_LSS   (OperatorBoolLss::new,   2, "<"),
    B_EQ_N  (OperatorBoolEqNot::new, 2, "!="),
    V_ASG   (OperatorAsg::new,       2, "="),
    B_TERN  (OperatorBoolTern::new,  3, "?", ":"),
    B_NOT   (OperatorBoolNot::new,   1, "!"),
    N_ADD   (OperatorNumAdd::new,    2, "+"),
    N_SUB   (OperatorNumSub::new,    2, "-"),
    N_MULT  (OperatorNumMult::new,   2, "*"),
    N_DIV   (OperatorNumDiv::new,    2, "/"),
    N_MOD   (OperatorNumMod::new,    2, "%"),
    N_POW   (OperatorNumPow::new,    2, "^"),
    // TODO: Read-only
    V_ARR_ELT(OperatorArrElt::new,   2, "[", "]"),
    ;

    final Operator op;
    final int ops;
    final String[] nttn;

    OperatorType(Supplier<Operator> supplier, int operands, String... notation) {
        op = supplier.get();
        ops = operands;
        nttn = notation;
    }

    VarValue eval(VarPool varPool, Var... o) {
        return op.passOperands(varPool, o);
    }

    VarOp resolve(String exp) {
        return ops > 1
                ? resolveMultipleOperands(exp)
                : resolveOneOperand(exp);
    }

    private VarOp resolveMultipleOperands(String exp) {
        char[] chars = exp.toCharArray();
        int lastOperandIdx = chars.length - 1;
        int lastOperatorIdx = nttn.length - 1;
        int brackets = 0;

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
            nextOperator = operands.size() < nttn.length
                    ? nttn[operands.size()]
                    : nttn[lastOperatorIdx];

            if (brackets == 0) {
                if (lastOperand.endsWith(nextOperator)) {
                    operands.add(lastOperand = lastOperand.substring(0, lastOperand.length() - nextOperator.length()));
                    lastOperand = new String();
                } else if (i == lastOperandIdx) {
                    operands.add(lastOperand);
                }
            }
        }

        return operands.size() <= 1 ? null : new VarOp(op, operands.stream()
                .map(Expressions::resolveVar).toArray(Var[]::new));
    }

    private VarOp resolveOneOperand(String exp) {
        String ref = null;
        if (exp.startsWith(nttn[0])) {
            ref = exp.substring(nttn[0].length());
        } else if (exp.endsWith(nttn[0])) {
            ref = exp.substring(0, exp.length() - nttn[0].length());
        }
        return ref == null ? null : new VarOp(op, Expressions.resolveVar(ref));
    }

}
