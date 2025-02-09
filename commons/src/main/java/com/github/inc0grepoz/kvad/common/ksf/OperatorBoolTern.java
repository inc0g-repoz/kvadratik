package com.github.inc0grepoz.kvad.common.ksf;

public class OperatorBoolTern implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        return new VarValue((boolean) o[0].getValue(varPool) ? o[1].getValue(varPool) : o[2].getValue(varPool));
    }

}
