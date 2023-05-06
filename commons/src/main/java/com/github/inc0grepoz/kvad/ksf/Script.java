package com.github.inc0grepoz.kvad.ksf;

import java.io.File;

import com.github.inc0grepoz.kvad.utils.Logger;

import lombok.Getter;

public class Script {

    public static final String EXTENSION = ".ksf";

    public static Script compile(File file, VarPool varPool) {
        ScriptTree tree = new ScriptTree(file);
        return new Script(file.getName(), tree, varPool);
    }

    final @Getter String name;
    final @Getter VarPool global;
    final ScriptPipeRoot pipeRoot;

    private Script(String name, ScriptTree tree, VarPool global) {
        this.name = name;
        this.global = global;

        // Converting string values into wrapped variables
        tree.target.wrapStrings_r(null).forEach(global::declare);
        Logger.info(tree.toString());

        // Compiling a pseudocode tree into a pipeline
        pipeRoot = (ScriptPipeRoot) tree.target.compile_r(null);
        pipeRoot.initHandlers();
        Logger.info("Found " + pipeRoot.handlers.size() + " handlers");
    }

    @Override
    public String toString() {
        return name;
    }

    void handleEvent(String name, Object event) {
        pipeRoot.streamHandlers(name).forEach(e -> e.pass(global, event));
    }

}
