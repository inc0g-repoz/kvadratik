package com.github.inc0grepoz.kvad.ksf.var;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.utils.Logger;

public abstract class Var {

    public abstract Object getValue();

    public Object fieldObj(String field) {
        Object sv = getValue();
        try {
            return sv.getClass().getField(field).get(sv);
        } catch (Throwable t) {
            Logger.error("Unknown field " + field + " of " + sv.getClass().getSimpleName());
        }
        return null;
    }

    public Object methodObj(String method, Object... args) {
        Object sv = getValue();
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

    private Method findMethod(Object value, String name, int argsCount) {
        return Stream.of(value.getClass().getMethods())
                .filter(m -> m.getParameterCount() == argsCount && m.getName().equals(name))
                .findAny().orElse(null);
    }

}
