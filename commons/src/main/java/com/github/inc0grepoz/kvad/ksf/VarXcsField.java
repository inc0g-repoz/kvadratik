package com.github.inc0grepoz.kvad.ksf;

public class VarXcsField extends VarXcs {

    VarXcsField(String name) {
        super(name);
    }

    @Override
    VarValue xcs_r(VarPool varPool, VarValue var) {
        VarValue passedVar = var == null ? varPool.get(name) : var;
        VarValue xcssedVar = passedVar.fieldVar(varPool, name);
        return nextXcs == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
