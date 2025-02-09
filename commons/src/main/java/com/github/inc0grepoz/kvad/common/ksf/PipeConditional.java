package com.github.inc0grepoz.kvad.common.ksf;

public abstract class PipeConditional extends Pipe {

    final Var boolExp;

    PipeConditional(Var boolExp) {
        this.boolExp = boolExp;
    }

}
