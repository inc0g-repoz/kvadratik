package com.github.inc0grepoz.kvad.ksf;

public class VarOp extends Var {

    Var var1, var2;
    Operator op;
    boolean negate;

    VarOp(Var var1, Var var2, Operator op, boolean negate) {
        this.var1 = var1;
        this.var2 = var2;
        this.op = op;
    }

    @Override
    Object getValue(VarPool varPool) {
        Object value = op.eval(varPool, var1, var2);
        return negate ? !(boolean) value : value;
    }

    @Override
    Var getVar(VarPool varPool) {
        return new VarValue(getValue(varPool));
    }

}
