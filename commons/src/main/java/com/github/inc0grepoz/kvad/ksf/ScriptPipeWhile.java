package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeWhile extends ScriptPipeConditional {

    ScriptPipeWhile(Var boolExp) {
        super(boolExp);
    }

    @Override
    boolean execute(VarPool varPool) {
        Object bool = boolExp.getValue(varPool);

        if (!(bool instanceof Boolean)) {
            Logger.error("Invalid boolean expression in a `while` loop");
            return false;
        }

        while ((boolean) bool) {
            if (!executeChildren(varPool)) {
                return false;
            }
            bool = boolExp.getValue(varPool);
        }

        return true;
    }

}
