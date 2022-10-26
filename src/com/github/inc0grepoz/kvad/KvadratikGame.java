package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Rectangle;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.GameFrame;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectBox;

@SuppressWarnings("serial")
public class KvadratikGame extends GameFrame {

    private final KvadratikCanvas canvas;
    private final PhysicsWorker physics;
    private final ConsoleWorker console;
    private final Controls controls;

    private Level level;

    {
        // Loading the level
        level = new Level(this);
        LevelObject[] lobj = {
                new LevelObjectBox(new Rectangle(0, 0, 100, 100), level),
        };
        for (int i = 0; i < lobj.length; i++) {
            level.getLevelObjects().add(lobj[i]);
        }

        // Rendering
        canvas = new KvadratikCanvas(this, 640, 480);
        add(canvas);
        canvas.setFrapsPerSecond(20);
        canvas.getWorker().start();

        // Controls
        controls = new Controls(this);
        addKeysListener(canvas);

        // Debug console
        console = new ConsoleWorker(this, 500L);
        console.start();

        // Physics
        physics = new PhysicsWorker(this, 100L);
        physics.start();
    }

    public KvadratikCanvas getCanvas() {
        return canvas;
    }

    public Controls getControls() {
        return controls;
    }

    public ConsoleWorker getConsole() {
        return console;
    }

    public Level getLevel() {
        return level;
    }

    public void addKeysListener(Canvas c) {
        c.addKeyListener(controls);
    }

}
