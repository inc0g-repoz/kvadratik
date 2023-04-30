package com.github.inc0grepoz.kvad.utils;

import java.util.List;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.ksf.Script;
import com.github.inc0grepoz.kvad.ksf.VarPool;

import lombok.Getter;

public class ScriptManager {

    private final Kvadratik kvad;
    private @Getter List<Script> scripts;

    public ScriptManager(Kvadratik kvad) {
        this.kvad = kvad;
    }

    public void loadScripts() {
        VarPool global = new VarPool();
        global.declare("kvad", kvad);
        scripts = kvad.getAssetsProvider().scripts("scripts", global);
    }

    public boolean unloadScript(String name) {
        return scripts.removeIf(s -> s.getName().equals(name));
    }

}
