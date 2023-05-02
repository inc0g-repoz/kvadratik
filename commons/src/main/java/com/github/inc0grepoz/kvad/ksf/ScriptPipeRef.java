package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeRef extends ScriptPipeXcs {

    String name;
    boolean newVar;

    ScriptPipeRef(Var var, String name, boolean newVar) {
        super(var);
        this.name = name;
        this.newVar = newVar;
    }

    @Override
    boolean execute(VarPool varPool) {
        if (newVar && varPool.get(name) != null) {
            Logger.error("Duplicate variable '" + name + "'");
            return false;
        }

        Object val = var.getValue(varPool);
        varPool.declare(name, val);
        return true;
    }

}
