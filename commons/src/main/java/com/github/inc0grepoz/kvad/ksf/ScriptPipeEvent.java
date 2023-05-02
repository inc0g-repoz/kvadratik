package com.github.inc0grepoz.kvad.ksf;

public class ScriptPipeEvent extends ScriptPipe {

    final String event;
    final String varName;

    ScriptPipeEvent(String event, String varName) {
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
