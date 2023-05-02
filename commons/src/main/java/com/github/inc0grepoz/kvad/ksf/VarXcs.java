package com.github.inc0grepoz.kvad.ksf;

public abstract class VarXcs extends Var {

    public VarXcs nextXcs;

    protected final String name;

    VarXcs(String name) {
        this.name = name;
    }

    @Override
    Var getVar(VarPool varPool) {
        return xcs_r(varPool, null);
    }

    abstract Var xcs_r(VarPool varPool, Var var);

}
