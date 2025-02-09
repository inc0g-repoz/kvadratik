package com.github.inc0grepoz.kvad.common.ksf;

import com.github.inc0grepoz.kvad.common.utils.Logger;

public class PipeWhile extends PipeConditional {

    PipeWhile(Var boolExp) {
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
