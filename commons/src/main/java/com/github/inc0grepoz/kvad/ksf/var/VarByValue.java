package com.github.inc0grepoz.kvad.ksf.var;

import lombok.Getter;

public class VarByValue extends Var {

    private final @Getter Object value;

    public VarByValue(Object value) {
        this.value = value;
    }

}
