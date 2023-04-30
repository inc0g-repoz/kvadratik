package com.github.inc0grepoz.kvad.ksf;

import java.io.File;

import com.github.inc0grepoz.kvad.utils.Logger;

import lombok.Getter;

public class Script {

    public static final String EXTENSION = ".ksf";

    public static Script compile(File file, VarPool varPool) {
        ScriptTree tree = new ScriptTree(file);
//      Logger.info(tree.toString());
        return new Script(file.getName(), tree, varPool);
    }

    final @Getter String name;
    final @Getter VarPool global;
    final ScriptPipeRoot pipeRoot;

    private Script(String name, ScriptTree tree, VarPool global) {
        this.name = name;
        this.global = global;

        // Compiling a pseudocode tree into a pipeline
        pipeRoot = tree.target.compileRecursively(this, global);
        pipeRoot.initHandlers();
        Logger.info("Found " + pipeRoot.handlers.size() + " handlers");
    }

    public void fireEvent(String name, Object event) {
        pipeRoot.findHandler(name).pass(global, event);
    }

}
