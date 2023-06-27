package com.github.inc0grepoz.kvad.ksf;

public class VarOp extends Var {

    final Var[] vars;
    Operator op;

    VarOp(Var... vars) {
        this.vars = vars;
    }

    @Override
    Object getValue(VarPool varPool) {
        return getVar(varPool).getValue(varPool);
    }

    @Override
    Var getVar(VarPool varPool) {
        return op.eval(varPool, vars);
    }

}
