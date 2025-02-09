package com.github.inc0grepoz.kvad.common.ksf;

public class OperatorBoolAnd implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        for (Var v : o) {
            if (!(boolean) v.getValue(varPool)) {
                return new VarValue(false);
            }
        }
        return new VarValue(true);
    }

}
