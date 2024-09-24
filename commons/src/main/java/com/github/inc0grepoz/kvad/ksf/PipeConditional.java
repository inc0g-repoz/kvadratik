package com.github.inc0grepoz.kvad.ksf;

public abstract class PipeConditional extends Pipe {

    final Var boolExp;

    PipeConditional(Var boolExp) {
        this.boolExp = boolExp;
    }

}
