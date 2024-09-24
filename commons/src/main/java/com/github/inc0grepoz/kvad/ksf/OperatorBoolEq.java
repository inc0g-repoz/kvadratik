package com.github.inc0grepoz.kvad.ksf;

public class OperatorBoolEq implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        Object v1 = o[0].getValue(varPool), v2 = o[1].getValue(varPool);
        if (v1 instanceof Number && v2 instanceof Number) {
            String d1s = String.valueOf(v1), d2s = String.valueOf(v2);
            return new VarValue(Double.valueOf(d1s).equals(Double.valueOf(d2s)));
        }
        return new VarValue(v1 == v2);
    }

}
