package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeConditional extends ScriptPipe {

    final Var boolExp;

    ScriptPipeConditional(Var boolExp) {
        this.boolExp = boolExp;
    }

    @Override
    boolean execute(VarPool varPool) {
        Object val = boolExp.getValue(varPool);

        if (!(val instanceof Boolean)) {
            Logger.error("Invalid boolean expression");
            return false;
        }

        return (boolean) val ? executeChildren(varPool) : true;
    }

}
