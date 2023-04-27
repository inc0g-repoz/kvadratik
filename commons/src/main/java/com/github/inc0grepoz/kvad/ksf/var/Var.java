package com.github.inc0grepoz.kvad.ksf.var;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.ksf.VarPool;
import com.github.inc0grepoz.kvad.utils.Logger;

public abstract class Var {

    public Object fieldObject(VarPool varPool, String field) {
        Object sv = getValue(varPool);
        try {
            return sv.getClass().getField(field).get(sv);
        } catch (Throwable t) {
            Logger.error("Unknown field " + field + " of " + sv.getClass().getSimpleName());
        }
        return null;
    }

    public Var fieldVar(VarPool varPool, String field) {
        return new VarByValue(fieldObject(varPool, field));
    }

    public Object methodObject(VarPool varPool, String method, Object... args) {
        Object sv = getValue(varPool);
        Method m = findMethod(sv, method, args.length);
        if (m == null) {
            Logger.error("Unknown method " + method + " of " + sv.getClass().getSimpleName());
        } else try {
            return m.invoke(sv, args);
        } catch (Throwable t) {
            Logger.error("Failed to access " + method + " of " + sv.getClass().getSimpleName());
        }
        return null;
    }

    public Var methodVar(VarPool varPool, String method, Var... varArgs) {
        Object[] args = Stream.of(varArgs).map(v -> v.getValue(varPool)).toArray(Object[]::new);
        return new VarByValue(methodObject(varPool, method, args));
    }

    private Method findMethod(Object value, String name, int argsCount) {
        return Stream.of(value.getClass().getMethods())
                .filter(m -> m.getParameterCount() == argsCount && m.getName().equals(name))
                .findAny().orElse(null);
    }

    public Object getValue(VarPool varPool) {
        return getVar(varPool).getValue(varPool);
    }

    public abstract Var getVar(VarPool varPool);

}
