package com.github.inc0grepoz.kvad.ksf;

public abstract class ScriptPipeConditional extends ScriptPipe {

    final Var boolExp;

    ScriptPipeConditional(Var boolExp) {
        this.boolExp = boolExp;
    }

}
