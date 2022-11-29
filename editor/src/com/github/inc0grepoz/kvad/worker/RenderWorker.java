package com.github.inc0grepoz.kvad.worker;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.editor.awt.CanvasRenderer;
import com.github.inc0grepoz.kvad.utils.Logger;

public class RenderWorker extends Worker {

    private final CanvasRenderer canvas;

    public RenderWorker(CanvasRenderer canvas) {
        super(33L);
        this.canvas = canvas;
    }

    @Override
    protected void work() {
        Graphics graphics = canvas.getGraphics();
        if (graphics == null) {
            Logger.error("Null graphics");
        } else {
            canvas.paint(graphics);
        }
    }

}
