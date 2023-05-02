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

    @Override
    Object getValue(VarPool varPool) {
        return getVar(varPool).getValue(varPool);
    }

    abstract VarValue xcs_r(VarPool varPool, VarValue var);

}
