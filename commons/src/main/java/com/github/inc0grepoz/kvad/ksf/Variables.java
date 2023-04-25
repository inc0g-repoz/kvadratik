package com.github.inc0grepoz.kvad.ksf;

import java.util.HashMap;
import java.util.Map;

public class Variables {

    static Object valueFromString(String string) {
        try {
            int value = Integer.valueOf(string);
            return value;
        } catch (Throwable t) {}

        try {
            float value = Float.valueOf(string);
            return value;
        } catch (Throwable t) {}

        try {
            double value = Double.valueOf(string);
            return value;
        } catch (Throwable t) {}

        return string;
    }

    private final Map<String, Accessible> vars = new HashMap<>();

    public void declare(String name, Object value) {
        vars.put(name, Accessible.initGlobalVar(value));
    }

    Accessible access(String name) {
        return vars.getOrDefault(name, null);
    }

    Variables copy() {
        Variables copy = new Variables();
        copy.vars.putAll(vars);
        return copy;
    }

}
