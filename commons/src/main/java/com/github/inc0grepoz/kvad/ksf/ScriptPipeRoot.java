package com.github.inc0grepoz.kvad.ksf;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.utils.Logger;

public class ScriptPipeRoot extends ScriptPipe {

    List<ScriptPipeEvent> handlers;

    Stream<ScriptPipeEvent> streamHandlers(String name) {
        return handlers.stream().filter(p -> p.event.equals(name));
    }

    @Override
    boolean execute(VarPool varPool) {
        Logger.error("Tried to execute a script pipe root");
        return false;
    }

    void initHandlers() {
        handlers = children.stream().filter(p -> p instanceof ScriptPipeEvent)
                .map(ScriptPipeEvent.class::cast).collect(Collectors.toList());
    }

}
