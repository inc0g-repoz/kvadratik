package com.github.inc0grepoz.kvad.ksf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.kvad.utils.Logger;

import lombok.Getter;

public class Script {

    public static Script compile(File file, Variables vars) {
        TreeScript tree = new TreeScript(file);
        Logger.info(tree.toString());
        return new Script(file.getName(), tree, vars);
    }

    final @Getter String name;
    final @Getter Variables global;
    final List<CompNode> nodes = new ArrayList<>();

    private Script(String name, TreeScript tree, Variables global) {
        this.name = name;
        this.global = global;
    }

}
