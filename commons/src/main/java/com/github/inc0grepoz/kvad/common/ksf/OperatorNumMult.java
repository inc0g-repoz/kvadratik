package com.github.inc0grepoz.kvad.common.ksf;

public class OperatorNumMult implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        String s = String.valueOf(o[0].getValue(varPool));
        double r = Double.valueOf(s);
        for (int i = 1; i < o.length; i++) {
            s = String.valueOf(o[i].getValue(varPool));
            r *= Double.valueOf(s);
        }
        return new VarValue(r % 1 == 0 ? (Object) (int) r : (Object) r);
    }

}
