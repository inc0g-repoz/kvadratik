package com.github.inc0grepoz.kvad.worker;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.utils.Logger;

public class RenderWorker extends Worker {

    private final KvadratikCanvas canvas;
    private boolean graphicsValid;

    public RenderWorker(KvadratikCanvas canvas) {
        super(16L);
        this.canvas = canvas;
    }

    @Override
    protected void work() {
        Graphics gfx = canvas.getGraphics();
        if (gfx == null) {
            Logger.error("Null graphics");
        } else {
            if (!graphicsValid) {
                graphicsValid = true;
                canvas.mainMenu();
            }
            canvas.paint(gfx);
        }
    }

}
