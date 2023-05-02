package com.github.inc0grepoz.kvad.ksf;

public class VarValue extends Var {

    Object value;

    VarValue(Object value) {
        this.value = value;
    }

    @Override
    Var getVar(VarPool varPool) {
        return this;
    }

    @Override
    public Object getValue(VarPool varPool) {
        return value;
    }

}
