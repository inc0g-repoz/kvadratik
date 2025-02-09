package com.github.inc0grepoz.kvad.server.worker;

import com.github.inc0grepoz.kvad.common.utils.TimeGap;
import com.github.inc0grepoz.kvad.common.worker.Worker;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

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
