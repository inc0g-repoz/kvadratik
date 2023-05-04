package com.github.inc0grepoz.kvad.ksf;

public class VarXcsMethod extends VarXcs {

    private final Var[] args;

    VarXcsMethod(String name, Var... args) {
        super(name);
        this.args = args;
    }

    @Override
    VarValue xcs_r(VarPool varPool, VarValue var) {
        VarValue xcssedVar = var.methodVar(varPool, name, args);
        return nextXcs == null || xcssedVar == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
