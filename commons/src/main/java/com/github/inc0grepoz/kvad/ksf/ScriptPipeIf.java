package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeIf extends ScriptPipeConditional {

    ScriptPipeOther elsePipe;

    ScriptPipeIf(Var boolExp) {
        super(boolExp);
    }

    @Override
    boolean execute(VarPool varPool) {
        Object bool = boolExp.getValue(varPool);

        if (!(bool instanceof Boolean)) {
            Logger.error("Invalid boolean expression in an `if` statement");
            return false;
        }

        return (boolean) bool ? executeChildren(varPool) : elsePipe == null
                ? true : elsePipe.executeChildren(varPool);
    }

}
