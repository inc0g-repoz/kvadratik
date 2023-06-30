package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeIf extends ScriptPipeConditional {

    ScriptPipeOther elsePipe;

    ScriptPipeIf(Var boolExp) {
        super(boolExp);
    }

    @Override
    boolean execute(VarPool varPool) {
        Object val = boolExp.getValue(varPool);

        if (!(val instanceof Boolean)) {
            Logger.error("Invalid boolean expression");
            return false;
        }

        return (boolean) val ? executeChildren(varPool) : elsePipe == null
                ? true : elsePipe.executeChildren(varPool);
    }

}
