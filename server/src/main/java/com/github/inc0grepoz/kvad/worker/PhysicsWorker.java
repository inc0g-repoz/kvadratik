package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class PhysicsWorker extends Worker {

    private final KvadratikServer kvad;

    public PhysicsWorker(KvadratikServer kvad, long delay) {
        super(delay);
        this.kvad = kvad;
    }

    @Override
    protected void work() {
        kvad.levels.forEach(level -> {

            // Moving the beings
            level.getBeings().forEach(Being::moveOn);

            // TODO: AI
        });
    }

}
