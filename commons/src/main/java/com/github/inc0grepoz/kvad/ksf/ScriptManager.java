package com.github.inc0grepoz.kvad.ksf;

import java.util.List;

import com.github.inc0grepoz.kvad.Kvadratik;

import lombok.Getter;

public class ScriptManager {

    private final Kvadratik kvad;
    private VarPool global;
    private @Getter List<Script> scripts;

    public ScriptManager(Kvadratik kvad) {
        this.kvad = kvad;
    }

    public void fireEvent(String name, Object event) {
        scripts.forEach(s -> s.handleEvent(name, event));
    }

    public void loadScript(String path) {
        scripts.add(kvad.getAssetsProvider().script(path, global));
    }

    public void loadScripts() {
        global = new VarPool() {{ declare("kvad", kvad); }};
        scripts = kvad.getAssetsProvider().scripts("scripts", global);
    }

    public boolean unloadScript(String name) {
        return scripts.removeIf(s -> s.getName().equals(name));
    }

}
