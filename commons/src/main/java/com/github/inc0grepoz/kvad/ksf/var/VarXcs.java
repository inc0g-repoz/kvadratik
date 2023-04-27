package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public abstract class VarXcs extends VarByName {

    public static Var resolve(String xcsStr) {
        return null;
    }

    public VarXcs nextXcs;

    VarXcs(VarPool varPool, String name) {
        super(varPool, name);
    }

    @Override
    public Var getVar(VarPool varPool) {
        return xcs_r(varPool, null);
    }

    protected abstract Var xcs_r(VarPool varPool, Var var);

}
