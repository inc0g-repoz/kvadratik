package com.github.inc0grepoz.kvad;

import java.awt.Canvas;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Game;

@SuppressWarnings("serial")
public class KvadratikGame extends Game {

    private final KvadratikCanvas canvas;
    private final PhysicsWorker physics;
    private final Controls controls;

    {

        // Physics
        physics = new PhysicsWorker(this, 100L);
        physics.start();

        // Rendering
        canvas = new KvadratikCanvas(this, 640, 480);
        add(canvas);
        canvas.getWorker().start();

        // Controls
        controls = new Controls();
        addKeysListener(canvas);

        /*
        setLocationRelativeTo(null);
        pack();

        Canvas canvas = new Canvas(null);
        canvas.setBackground(Color.BLACK);
        canvas.prepareImage(image, canvas);
        canvas.imageUpdate(image, ALLBITS, MAXIMIZED_BOTH, ABORT, WIDTH, HEIGHT);
        add(canvas);
        */
    }

    public KvadratikCanvas getCanvas() {
        return canvas;
    }

    public Controls getControls() {
        return controls;
    }

    public void addKeysListener(Canvas c) {
        c.addKeyListener(controls);
    }

}
