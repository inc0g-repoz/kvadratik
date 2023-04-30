package com.github.inc0grepoz.kvad.ksf;

public class ScriptPipeEvent extends ScriptPipe {

    final String event;
    final String varName;

    ScriptPipeEvent(String event, String varName) {
        this.event = event;
        this.varName = varName;
    }

    @Override
    void execute(VarPool varPool) {
        VarPool vpCopy = varPool.copy();
        children.forEach(p -> p.execute(vpCopy));
    }

    void pass(VarPool global, Object event) {
        VarPool vpCopy = global.copy();
        vpCopy.declare(varName, event);
        execute(vpCopy);
    }

}
