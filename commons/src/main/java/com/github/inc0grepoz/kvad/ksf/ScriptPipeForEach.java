package com.github.inc0grepoz.kvad.ksf;

import java.lang.reflect.Array;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeForEach extends ScriptPipe {

    final String paramName;
    final Var iterVar;

    ScriptPipeForEach(String paramName, Var iterVar) {
        this.paramName = paramName;
        this.iterVar = iterVar;
    }

    @Override
    boolean execute(VarPool varPool) {
        Var iterableVar = iterVar.getVar(varPool);
        Object iterable = iterableVar.getValue(varPool);

        if (iterable.getClass().isArray()) {
            int length = Array.getLength(iterable);
            for (int i = 0; i < length; i++) {
                varPool.declare(paramName, Array.get(iterable, i));
                executeChildren(varPool);
            }
        } else if (iterable instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) iterable;
            for (Object param : iter) {
                varPool.declare(paramName, param);
                executeChildren(varPool);
            }
        } else {
            Logger.error(paramName + " is not an array or instance of Iterable [" + lineIndex + "]");
            return false;
        }

        return true;
    }

}
