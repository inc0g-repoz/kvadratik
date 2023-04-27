package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public abstract class VarXcs extends Var {

    public static Var resolve(String xcsStr) {
        return null;
    }

    protected final String name;
    protected final VarXcs nextXcs;

    VarXcs(VarPool varPool, String name, VarXcs nextXcs) {
        this.name = name;
        this.nextXcs = nextXcs;
    }

    protected abstract VarXcs xcs(Var var);

}
