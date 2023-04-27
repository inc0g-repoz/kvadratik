package com.github.inc0grepoz.kvad.ksf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Script {

    public static final String EXTENSION = ".ksf";

    public static Script compile(File file, VarPool varPool) {
        ScriptTreeScript tree = new ScriptTreeScript(file);
//      Logger.info(tree.toString());
        return new Script(file.getName(), tree, varPool);
    }

    final @Getter String name;
    final @Getter VarPool global;
    final List<ScriptCompNode> nodes = new ArrayList<>();

    private Script(String name, ScriptTreeScript tree, VarPool global) {
        this.name = name;
        this.global = global;
    }

}