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
        if (newVar) {
            if (varPool.get(name) != null) {
                Logger.error("Duplicate variable '" + name + "'");
                return false;
            }
            varPool.declare(name, var.getValue(varPool));
        } else {
            VarValue varVal = varPool.get(name);
            if (varVal == null) {
                Logger.error("Unknown variable '" + name + "'");
                return false;
            }
            varVal.value = var.getValue(varPool);
        }
        return true;
    }

}
