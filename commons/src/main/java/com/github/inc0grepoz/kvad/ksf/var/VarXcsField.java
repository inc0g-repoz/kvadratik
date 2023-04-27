package com.github.inc0grepoz.kvad.ksf.var;

import com.github.inc0grepoz.kvad.ksf.VarPool;

public class VarXcsField extends VarXcs {

    VarXcsField(VarPool varPool, String name, VarXcs nextXcs) {
        super(varPool, name, nextXcs);
    }

    @Override
    public Object getValue() {
        
        return null;
    }

    @Override
    protected VarXcs xcs(Var var) {
        
        return null;
    }

}
