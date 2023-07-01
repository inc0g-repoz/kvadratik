package com.github.inc0grepoz.kvad.worker;

import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.ksf.Event;
import com.github.inc0grepoz.kvad.ksf.ScriptManager;

public class AsyncEventsWorker extends Worker {

    ScriptManager scriptMan;
    Queue<Event> events = new LinkedList<>();

    protected AsyncEventsWorker(ScriptManager scriptMan, long delay) {
        super(delay);
        this.scriptMan = scriptMan;
    }

    public void queue(Event event) {
        events.add(event);
    }

    @Override
    protected void work() {
        scriptMan.fireEvent(events.poll());
    }

}
