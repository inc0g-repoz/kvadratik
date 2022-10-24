package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Rectangle;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Game;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectBox;

@SuppressWarnings("serial")
public class KvadratikGame extends Game {

    private final KvadratikCanvas canvas;
    private final PhysicsWorker physics;
    private final Controls controls;

    {
        Level level = getLevel();
        LevelObject[] lobj = {
                new LevelObjectBox(new Rectangle(0, 0, 25, 25), level),
                new LevelObjectBox(new Rectangle(100, 100, 25, 25), level)
        };
        for (int i = 0; i < lobj.length; i++) {
            level.getLevelObjects().add(lobj[i]);
        }

        // Physics
        physics = new PhysicsWorker(this, 100L);
        physics.start();

        // Rendering
        canvas = new KvadratikCanvas(this, 640, 480);
        add(canvas);
        canvas.setFrapsPerSecond(10);
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
