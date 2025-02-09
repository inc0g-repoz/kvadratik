package com.github.inc0grepoz.kvad.common.ksf;

import java.lang.reflect.Array;

public class OperatorArrElt implements Operator {

    @Override
    public VarValue passOperands(VarPool varPool, Var... o) {
        return new VarValue(Array.get(o[0].getValue(varPool), (int) o[1].getValue(varPool)));
    }

}
