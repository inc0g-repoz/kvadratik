package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarByName extends Var {

    private final VarPool varPool;
    private final String name;

    public VarByName(VarPool varPool, String name) {
        this.varPool = varPool;
        this.name = name;
    }

    public Object getValue() {
        return varPool.get(name);
    }

}
