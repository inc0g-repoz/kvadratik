package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarByName extends Var {

    protected final String name;

    public VarByName(VarPool varPool, String name) {
        this.name = name;
    }

    @Override
    public Var getVar(VarPool varPool) {
        return varPool.get(name);
    }

}
