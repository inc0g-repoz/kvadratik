package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.List;

public abstract class ScriptCompNode {

    ScriptCompNode parent;
    List<ScriptCompNode> children = new LinkedList<>();

    abstract void execute(VarPool varPool);

}
