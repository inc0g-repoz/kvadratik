package com.github.inc0grepoz.kvad.ksf;

import java.util.HashMap;
import java.util.Map;

import com.github.inc0grepoz.kvad.ksf.var.Var;
import com.github.inc0grepoz.kvad.ksf.var.VarByValue;

public class VarPool {

    private final Map<String, Var> vars = new HashMap<>();

    public void declare(String name, Object value) {
        vars.put(name, new VarByValue(value));
    }

    public Var get(String name) {
        return vars.getOrDefault(name, null);
    }

    public VarPool copy() {
        VarPool copy = new VarPool();
        copy.vars.putAll(vars);
        return copy;
    }

}
