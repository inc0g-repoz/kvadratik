package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarXcsMethod extends VarXcs {

    private final Var[] args;

    public VarXcsMethod(VarPool varPool, String name, Var... args) {
        super(varPool, name);
        this.args = args;
    }

    @Override
    protected Var xcs_r(VarPool varPool, Var var) {
        Var passedVar = var == null ? varPool.get(name) : var;
        Var xcssedVar = passedVar.methodVar(varPool, name, args);
        return nextXcs == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
