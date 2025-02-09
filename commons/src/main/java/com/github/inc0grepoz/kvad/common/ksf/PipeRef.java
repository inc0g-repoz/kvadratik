package com.github.inc0grepoz.kvad.common.ksf;

import com.github.inc0grepoz.kvad.common.utils.Logger;

public class PipeRef extends PipeXcs {

    static PipeRef resolve(String line) {
        String[] leftRight = line.split(" *= *", 2);
        String name = leftRight[0].substring(4);
        return new PipeRef(Expressions.resolveVar(leftRight[1]), name);
    }

    String name;

    PipeRef(Var var, String name) {
        super(var);
        this.name = name;
    }

    @Override
    boolean execute(VarPool varPool) {
        if (varPool.get(name) != null) {
            Logger.error("Duplicate variable '" + name + "'");
            return false;
        }
        varPool.declare(name, var.getValue(varPool));
        return true;
    }

}
