package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.Queue;

public abstract class ScriptPipe {

    ScriptPipe parent;

    final Queue<ScriptPipe> children = new LinkedList<>();

    boolean executeChildren(VarPool varPool) {
        VarPool vpCopy = varPool.copy();
        for (ScriptPipe p : children) {
            if (!p.execute(vpCopy)) {
                return false;
            }
        }
        return true;
    }

    abstract boolean execute(VarPool varPool);

}
