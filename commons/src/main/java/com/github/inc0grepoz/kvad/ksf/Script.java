package com.github.inc0grepoz.kvad.ksf;

import java.io.File;

import lombok.Getter;

public class Script {

    public static final String EXTENSION = ".ksf";

    public static Script compile(File file, VarPool varPool) {
        ScriptTree tree = new ScriptTree(file);
//      Logger.info(tree.toString());

        // Compiling a pseudocode tree into a pipeline
        Script script = new Script(file.getName(), tree, varPool);
        script.pipeRoot = tree.target.compileRecursively(script, varPool);
        script.pipeRoot.initHandlers();
        return script;
    }

    final @Getter String name;
    final @Getter VarPool global;
    ScriptPipeRoot pipeRoot;

    private Script(String name, ScriptTree tree, VarPool global) {
        this.name = name;
        this.global = global;
    }

    public void fireEvent(String name, Object event) {
        pipeRoot.findHandler(name).pass(global, event);
    }

}
