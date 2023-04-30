package com.github.inc0grepoz.kvad.ksf;

public class ScriptPipeEvent extends ScriptPipe {

    String event;
    String arg;

    ScriptPipeEvent(String event) {
        this.event = event;
    }

    public void pass(VarPool global, Object event) {
        VarPool vpCopy = global.copy();
        vpCopy.declare(arg, event);
        execute(vpCopy);
    }

    @Override
    void execute(VarPool varPool) {
        VarPool vpCopy = varPool.copy();
        children.forEach(p -> p.execute(vpCopy));
    }

}
