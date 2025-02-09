package com.github.inc0grepoz.kvad.common.ksf;

import java.util.HashMap;
import java.util.Map;

public class VarPool {

    private final Map<String, VarValue> vars = new HashMap<>();

    void declareAll(VarPool vp) {
        vars.putAll(vp.vars);
    }

    void declare(String name, Object value) {
        vars.put(name, new VarValue(value));
    }

    void delete(String name) {
        vars.remove(name);
    }

    VarValue get(String name) {
        return vars.getOrDefault(name, null);
    }

    VarPool copy() {
        VarPool copy = new VarPool();
        copy.vars.putAll(vars);
        return copy;
    }

}
