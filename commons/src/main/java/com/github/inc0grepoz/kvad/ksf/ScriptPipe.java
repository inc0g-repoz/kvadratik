package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.List;

public abstract class ScriptPipe {

    ScriptPipe parent;
    List<ScriptPipe> children = new LinkedList<>();

    abstract void execute(VarPool varPool);

}
