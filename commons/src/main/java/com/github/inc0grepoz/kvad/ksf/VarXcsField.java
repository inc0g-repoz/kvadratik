package com.github.inc0grepoz.kvad.ksf;

public class VarXcsField extends VarXcs {

    VarXcsField(String name) {
        super(name);
    }

    @Override
    Var xcs_r(VarPool varPool, Var var) {
        Var passedVar = var == null ? varPool.get(name) : var;
        Var xcssedVar = passedVar.fieldVar(varPool, name);
        return nextXcs == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
