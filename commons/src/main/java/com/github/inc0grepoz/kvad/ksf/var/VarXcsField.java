package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarXcsField extends VarXcs {

    public VarXcsField(VarPool varPool, String name) {
        super(varPool, name);
    }

    @Override
    protected Var xcs_r(VarPool varPool, Var var) {
        Var passedVar = var == null ? varPool.get(name) : var;
        Var xcssedVar = passedVar.fieldVar(varPool, name);
        return nextXcs == null ? xcssedVar : nextXcs.xcs_r(varPool, xcssedVar);
    }

}
