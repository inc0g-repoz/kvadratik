package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

import lombok.Getter;

public class VarByValue extends Var {

    private final @Getter Object value;

    public VarByValue(Object value) {
        this.value = value;
    }

    @Override
    public Var getVar(VarPool varPool) {
        return this;
    }

}
