package com.github.inc0grepoz.kvad.ksf;

public class OperatorNumDiv implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        String d1s = String.valueOf(o[0].getValue(varPool));
        String d2s = String.valueOf(o[1].getValue(varPool));
        double r = Double.valueOf(d1s) / Double.valueOf(d2s);
        return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
    }

}
