package com.github.inc0grepoz.kvad.common.ksf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.common.utils.Logger;

public abstract class Var {

    private static Method findMethod(Object value, String name, int argsCount) {
        return Stream.of(value.getClass().getMethods())
                .filter(m -> m.getParameterCount() == argsCount && m.getName().equals(name))
                .findAny().orElse(null);
    }

    private static String typeName(Object o) {
        return o == null ? "a null value" : o.getClass().isAnonymousClass() ? "anonymous class instance" : o.getClass().getSimpleName();
    }

    Object fieldObject(VarPool varPool, String field) {
        Object sv = getValue(varPool);
        try {
            Field f = sv.getClass().getField(field);
            if (!Modifier.isPublic(f.getModifiers())) {
                Logger.error("Tried to access a non-public field " + field + " of " + typeName(sv));
            } else {
                f.setAccessible(true);
                return f.get(sv);
            }
        } catch (IllegalArgumentException e) {
            Logger.error("Whatever fields have to do with arguments, but they are wrong");
        } catch (IllegalAccessException e) {
            Logger.error("Field " + field + " of " + typeName(sv) + " cannot be accessed");
        } catch (NoSuchFieldException e) {
            Logger.error("Unknown field " + field + " of " + typeName(sv));
        } catch (SecurityException e) {
            Logger.error("Field " + field + " of " + typeName(sv) + " cannot be accessed");
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
            Logger.error("Unknown method " + method + " of " + typeName(sv));
        } else if (!Modifier.isPublic(m.getModifiers())) {
            Logger.error("Tried to access a non-public method " + method + " of " + typeName(sv));
        } else {
            m.setAccessible(true);
            try {
                return m.invoke(sv, args);
            } catch (IllegalAccessException e) {
                Logger.error("Failed to access " + method + " of " + typeName(sv));
            } catch (IllegalArgumentException e) {
                Logger.error("Called " + method + "(...) from " + typeName(sv) + " with invalid arguments");
            } catch (InvocationTargetException e) {
                Logger.error("Invalid method call target (" + typeName(sv) + ")");
            }
        }
        return null;
    }

    VarValue methodVar(VarPool varPool, String method, Var... varArgs) {
        Object[] args = Stream.of(varArgs).map(v -> v.getValue(varPool)).toArray(Object[]::new);
        return new VarValue(methodObject(varPool, method, args));
    }

    abstract Object getValue(VarPool varPool);

    abstract Var getVar(VarPool varPool);

}
