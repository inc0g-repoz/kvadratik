package com.github.inc0grepoz.kvad.common.ksf;

public abstract class VarXcs extends Var {

    boolean negate;
    VarXcs nextXcs;

    final String name;

    VarXcs(String name) {
        this.name = name;
    }

    @Override
    VarValue getVar(VarPool varPool) {
        return xcs_r(varPool, null);
    }

    @Override
    Object getValue(VarPool varPool) {
        Object v = getVar(varPool).getValue(varPool);
        return negate ? !(boolean) v : v;
    }

    abstract VarValue xcs_r(VarPool varPool, VarValue var);

}
