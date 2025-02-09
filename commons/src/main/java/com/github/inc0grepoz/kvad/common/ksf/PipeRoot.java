package com.github.inc0grepoz.kvad.common.ksf;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.common.utils.Logger;

public class PipeRoot extends Pipe {

    List<PipeEvent> handlers;

    Stream<PipeEvent> streamHandlers(String name) {
        return handlers.stream().filter(p -> p.event.equals(name));
    }

    @Override
    boolean execute(VarPool varPool) {
        Logger.error("Tried to execute a script pipe root");
        return false;
    }

    void initHandlers() {
        handlers = children.stream().filter(p -> p instanceof PipeEvent)
                .map(PipeEvent.class::cast).collect(Collectors.toList());
    }

}
