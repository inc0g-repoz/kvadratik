package com.github.inc0grepoz.kvad.ksf;

public class OperatorBoolEqNot implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        return new VarValue(!o[0].getValue(varPool).equals(o[1].getValue(varPool)));
    }

}
