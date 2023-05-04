package com.github.inc0grepoz.kvad.ksf;

public class VarXcsField extends VarXcs {

    VarXcsField(String name) {
        super(name);
    }

    @Override
    VarValue xcs_r(VarPool varPool, VarValue var) {
        VarValue xcssedVar = var == null ? varPool.get(name) : var.fieldVar(varPool, name);
        return nextXcs == null || xcssedVar == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
