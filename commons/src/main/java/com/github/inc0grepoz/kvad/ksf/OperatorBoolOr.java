package com.github.inc0grepoz.kvad.ksf;

public class OperatorBoolOr implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        for (Var v : o) {
            if ((boolean) v.getValue(varPool)) {
                return new VarValue(true);
            }
        }
        return new VarValue(false);
    }

}
