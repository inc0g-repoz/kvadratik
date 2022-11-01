package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

@SuppressWarnings("serial")
public class KvadratikGame extends Frame {

    private final static AssetsManager ASSETS = new AssetsManager();

    public static AssetsManager getAssets() {
        return ASSETS;
    }

    private final KvadratikCanvas canvas;
    private final PhysicsWorker physics;
    private final ConsoleWorker console;
    private final Controls controls;

    private Level level;

    {
        WindowAdapter adapter = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }

        };
        addWindowListener(adapter);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            adapter.windowClosed(null);
        }));

        setTitle("kvadratik");
        applyIcon("assets/icon.png");

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

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
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
