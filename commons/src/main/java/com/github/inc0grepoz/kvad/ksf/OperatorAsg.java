package com.github.inc0grepoz.kvad.ksf;

public class OperatorAsg implements Operator {


    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        VarValue vv = (VarValue) o[0].getVar(varPool);
        vv.value = o[1].getValue(varPool);
        return vv;
    }

}
