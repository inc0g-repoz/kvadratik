package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeWhile extends ScriptPipeConditional {

    ScriptPipeWhile(Var boolExp) {
        super(boolExp);
    }

    @Override
    boolean execute(VarPool varPool) {
        Object val = boolExp.getValue(varPool);

        if (!(val instanceof Boolean)) {
            Logger.error("Invalid boolean expression");
            return false;
        }

        while ((boolean) val) {
            if (!executeChildren(varPool)) {
                return false;
            }
            val = boolExp.getValue(varPool);
        }

        return true;
    }

}
