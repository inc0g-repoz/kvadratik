package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.Worker;

public class RenderWorker extends Worker {

    private final KvadratikCanvas canvas;

    public RenderWorker(KvadratikCanvas canvas) {
        super(33L);
        this.canvas = canvas;
    }

    public void work() {
        canvas.update();
    }

}
