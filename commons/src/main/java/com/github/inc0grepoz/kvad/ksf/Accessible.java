package com.github.inc0grepoz.kvad.ksf;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.utils.Logger;

public class Accessible {

    public static Accessible initGlobalVar(Object obj) {
        Accessible xs = new Accessible();
        xs.value = obj;
        return xs;
    }

    public static Accessible initLocalScopeAccessible(String name) {
        Accessible xs = new Accessible();
        xs.name = name;
        return xs;
    }

    private static Object accessField(Object obj, String field) {
        try {
            return obj.getClass().getField(field).get(obj);
        } catch (Throwable t) {
            Logger.error("Unknown field " + field + " of " + obj.getClass().getSimpleName());
            return null;
        }
    }

    private static Method findMethod(Object obj, String name, int argsCount) {
        return Stream.of(obj.getClass().getMethods())
                .filter(m -> m.getParameterCount() == argsCount && m.getName().equals(name))
                .findAny().orElse(null);
    }

    private static Object invoke(Object obj, String method, Object... args) {
        Method m = findMethod(obj, method, args.length);
        if (m == null) {
            Logger.error("Unknown method " + method + " of " + obj.getClass().getSimpleName());
            return null;
        }
        try {
            return m.invoke(obj, args);
        } catch (Throwable t) {
            Logger.error("You're not supposed to see this");
            return null;
        }
    }

    CompNode node;
    String name;

    boolean methodInvokation;
    Object value;
    Object[] args;

    private Accessible() {}

    public Accessible access() {
        Accessible xs = new Accessible();
        xs.value = methodInvokation ? invoke(this.value, name, args) : accessField(this.value, name);
        return xs;
    }

}
