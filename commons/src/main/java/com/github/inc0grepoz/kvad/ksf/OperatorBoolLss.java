package com.github.inc0grepoz.kvad.ksf;

public class OperatorBoolLss implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        String d1s = String.valueOf(o[0].getValue(varPool));
        String d2s = String.valueOf(o[1].getValue(varPool));
        return new VarValue(Double.valueOf(d1s) < Double.valueOf(d2s));
    }

}
