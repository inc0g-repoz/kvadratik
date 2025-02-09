package com.github.inc0grepoz.kvad.common.ksf;

public class PipeEvent extends Pipe {

    final String event;
    final String varName;

    PipeEvent(String event, String varName) {
        this.event = event;
        this.varName = varName;
    }

    @Override
    boolean execute(VarPool varPool) {
        return executeChildren(varPool);
    }

    void pass(VarPool global, Object event) {
        VarPool vpCopy = global.copy();
        vpCopy.declare(varName, event);
        execute(vpCopy);
    }

}
