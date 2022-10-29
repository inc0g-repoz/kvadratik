package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Color;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.GameFrame;
import com.github.inc0grepoz.kvad.entities.level.Level;

@SuppressWarnings("serial")
public class KvadratikGame extends GameFrame {

    private final KvadratikCanvas canvas;
    private final PhysicsWorker physics;
    private final ConsoleWorker console;
    private final Controls controls;

    private Level level;

    {
        // Loading the level
        level = new Level(this, getAssets().readXml("assets/levels/whitespace.xml"));

        // Rendering
        canvas = new KvadratikCanvas(this, 640, 480);
        canvas.setBackground(Color.BLACK);
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
        physics = new PhysicsWorker(this, 50L);
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
