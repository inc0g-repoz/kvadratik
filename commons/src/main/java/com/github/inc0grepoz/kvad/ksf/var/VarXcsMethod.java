package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarXcsMethod extends VarXcs {

    private final Var[] args;

    VarXcsMethod(VarPool varPool, String name, VarXcs nextXcs, Var... args) {
        super(varPool, name, nextXcs);
        this.args = args;
    }

    @Override
    public Object getValue() {
        
        return null;
    }

    @Override
    protected VarXcs xcs(Var var) {
        // TODO Auto-generated method stub
        return null;
    }

}
