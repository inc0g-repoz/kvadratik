package com.github.inc0grepoz.kvad.ksf;

import java.util.LinkedList;
import java.util.List;

public abstract class CompNode {

    CompNode parent;
    List<CompNode> children = new LinkedList<>();

    abstract void execute(Variables vars, Object... args);

}
