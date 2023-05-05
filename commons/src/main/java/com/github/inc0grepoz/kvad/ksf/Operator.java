package com.github.inc0grepoz.kvad.ksf;

@FunctionalInterface
interface Evaluator {
    VarValue passOperands(VarPool varPool, Var... o);
}

public enum Operator {

    /* B_NOT('!', (vp, o) -> new VarValue(
        !(boolean) o[0].getValue(vp)
    )), */
    B_AND("&&", (vp, o) -> new VarValue(
        (boolean) o[0].getValue(vp) && (boolean) o[1].getValue(vp)
    )),
    B_OR("||", (vp, o) -> new VarValue(
        (boolean) o[0].getValue(vp) || (boolean) o[1].getValue(vp)
    )),
    B_GTR('>', (vp, o) -> {
        return new VarValue((double) o[1].getValue(vp) > (double) o[2].getValue(vp));
    }),
    B_LSS('<', (vp, o) -> {
        return new VarValue((double) o[1].getValue(vp) < (double) o[2].getValue(vp));
    }),
    B_EQ_GTR(">=", (vp, o) -> {
        return new VarValue((double) o[1].getValue(vp) >= (double) o[2].getValue(vp));
    }),
    B_EQ_LSS("<=", (vp, o) -> {
        return new VarValue((double) o[1].getValue(vp) <= (double) o[2].getValue(vp));
    }),
    N_ADD('+', (vp, o) -> {
        double r = (double) o[1].getValue(vp) + (double) o[2].getValue(vp);
        return new VarValue(r % 1 == 0 ? (int) r : r);
    }),
    N_SUB('-', (vp, o) -> {
        double r = (double) o[1].getValue(vp) - (double) o[2].getValue(vp);
        return new VarValue(r % 1 == 0 ? (int) r : r);
    }),
    N_MULT('*', (vp, o) -> {
        double r = (double) o[1].getValue(vp) * (double) o[2].getValue(vp);
        return new VarValue(r % 1 == 0 ? (int) r : r);
    }),
    N_DIV('/', (vp, o) -> {
        double r = (double) o[1].getValue(vp) / (double) o[2].getValue(vp);
        return new VarValue(r % 1 == 0 ? (int) r : r);
    }),
    N_MOD('%', (vp, o) -> {
        double r = (double) o[1].getValue(vp) % (double) o[2].getValue(vp);
        return new VarValue(r % 1 == 0 ? (int) r : r);
    });

    final char[] nttn;
    final Evaluator eval;

    Operator(char[] nttn, Evaluator eval) {
        this.nttn = nttn;
        this.eval = eval;
    }

    Operator(char nttn, Evaluator eval) {
        this(new char[] { nttn }, eval);
    }

    Operator(String nttn, Evaluator eval) {
        this(nttn.toCharArray(), eval);
    }

    VarValue eval(VarPool varPool, Var... o) {
        return eval.passOperands(varPool, o);
    }

}
