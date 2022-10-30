package com.github.inc0grepoz.kvad;

import java.awt.Graphics;

import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.utils.Logger;

public class RenderWorker extends Worker {

    private final KvadratikCanvas canvas;

    public RenderWorker(KvadratikCanvas canvas) {
        super(33L);
        this.canvas = canvas;
    }

    public void work() {
        Graphics graphics = canvas.getGraphics();
        if (graphics == null) {
            Logger.error("Null graphics");
        } else {
            canvas.paint(graphics);
        }
    }

}
