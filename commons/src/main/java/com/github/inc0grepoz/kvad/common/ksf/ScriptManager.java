package com.github.inc0grepoz.kvad.common.ksf;

import java.util.List;

import com.github.inc0grepoz.kvad.common.Kvadratik;

import lombok.Getter;

public class ScriptManager {

    static boolean debugMode = true;

    private final Kvadratik kvad;
    private VarPool global;
    private @Getter List<Script> scripts;

    public ScriptManager(Kvadratik kvad) {
        this.kvad = kvad;
    }

    public void fireEvent(Event event) {
        scripts.forEach(s -> s.handleEvent(event.getName(), event));
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
