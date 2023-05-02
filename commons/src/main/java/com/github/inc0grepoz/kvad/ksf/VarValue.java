package com.github.inc0grepoz.kvad.ksf;

import lombok.Getter;

public class VarValue extends Var {

    private final @Getter Object value;

    VarValue(Object value) {
        this.value = value;
    }

    @Override
    Var getVar(VarPool varPool) {
        return this;
    }

}
