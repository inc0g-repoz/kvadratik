package com.github.inc0grepoz.kvad.ksf;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.utils.Logger;

public abstract class Var {

    Object fieldObject(VarPool varPool, String field) {
        Object sv = getValue(varPool);
        try {
            return sv.getClass().getField(field).get(sv);
        } catch (Throwable t) {
            Logger.error("Unknown field " + field + " of " + sv.getClass().getSimpleName());
        }
        return null;
    }

    VarValue fieldVar(VarPool varPool, String field) {
        return new VarValue(fieldObject(varPool, field));
    }

    Object methodObject(VarPool varPool, String method, Object... args) {
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

    VarValue methodVar(VarPool varPool, String method, Var... varArgs) {
        Object[] args = Stream.of(varArgs).map(v -> v.getValue(varPool)).toArray(Object[]::new);
        return new VarValue(methodObject(varPool, method, args));
    }

    abstract Object getValue(VarPool varPool);

    abstract Var getVar(VarPool varPool);

    private Method findMethod(Object value, String name, int argsCount) {
        return Stream.of(value.getClass().getMethods())
                .filter(m -> m.getParameterCount() == argsCount && m.getName().equals(name))
                .findAny().orElse(null);
    }

}
