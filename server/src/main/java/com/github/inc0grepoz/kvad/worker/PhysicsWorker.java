package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.TimeGap;

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
            long gap = TimeGap.get();
            level.getBeings().forEach(b -> b.moveOn(gap));

            // TODO: AI
        });
    }

}
