package com.github.inc0grepoz.kvad.ksf;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeFor extends ScriptPipeConditional {

    final ScriptPipeRef param;
    final Var op;

    ScriptPipeFor(ScriptPipeRef param, Var boolExp, Var op) {
        super(boolExp);
        this.param = param;
        this.op = op;
    }

    @Override
    boolean execute(VarPool varPool) {
        if (param != null) {
            param.execute(varPool);
        }

        Object bool = boolExp == null ? true : boolExp.getValue(varPool);

        if (!(bool instanceof Boolean)) {
            Logger.error("Invalid boolean expression in a `for` loop");
            return false;
        }

        while ((boolean) bool) {
            if (!executeChildren(varPool)) {
                return false;
            }
            op.getVar(varPool); // Mutating the parameter
            bool = boolExp.getValue(varPool);
        }

        return true;
    }

}
