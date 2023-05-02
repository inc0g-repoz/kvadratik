package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeIf extends ScriptPipe {

    final Var boolExp;

    ScriptPipeIf(Var boolExp) {
        this.boolExp = boolExp;
    }

    @Override
    boolean execute(VarPool varPool) {
        VarPool vpCopy = varPool.copy();
        Object val = boolExp.getValue(vpCopy);

        if (!(val instanceof Boolean)) {
            Logger.error("Invalid boolean expression");
            return false;
        }

        return (boolean) val ? executeChildren(varPool) : true;
    }

}
