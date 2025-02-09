package com.github.inc0grepoz.kvad.common.ksf;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Pipe {

    int lineIndex;
    Pipe parent;

    final Queue<Pipe> children = new LinkedList<>();

    boolean executeChildren(VarPool varPool) {
        VarPool vpCopy = varPool.copy();
        for (Pipe p : children) {
            if (!p.execute(vpCopy)) {
                return false;
            }
        }
        return true;
    }

    abstract boolean execute(VarPool varPool);

}
