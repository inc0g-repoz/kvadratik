package com.github.inc0grepoz.kvad.ksf;

public class VarXcsMethod extends VarXcs {

    private final Var[] args;

    VarXcsMethod(String name, Var... args) {
        super(name);
        this.args = args;
    }

    @Override
    Var xcs_r(VarPool varPool, Var var) {
        Var passedVar = var == null ? varPool.get(name) : var;
        Var xcssedVar = passedVar.methodVar(varPool, name, args);
        return nextXcs == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
