package com.github.inc0grepoz.kvad.ksf;

import java.util.HashMap;
import java.util.Map;

public class VarPool {

    private final Map<String, Var> vars = new HashMap<>();

    void declare(String name, Object value) {
        vars.put(name, new VarValue(value));
    }

    void delete(String name) {
        vars.remove(name);
    }

    Var get(String name) {
        return vars.getOrDefault(name, null);
    }

    VarPool copy() {
        VarPool copy = new VarPool();
        copy.vars.putAll(vars);
        return copy;
    }

}
