package com.github.inc0grepoz.kvad.common.ksf;

public class OperatorBoolNot implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        return new VarValue(!(boolean) o[0].getValue(varPool));
    }

}
