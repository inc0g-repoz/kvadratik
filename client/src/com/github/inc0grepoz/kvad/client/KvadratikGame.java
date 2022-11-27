package com.github.inc0grepoz.kvad.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import com.github.inc0grepoz.kvad.entities.BeingFactory;
import com.github.inc0grepoz.kvad.entities.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

@SuppressWarnings("serial")
public class KvadratikGame extends Frame {

    public static final AssetsManager ASSETS = new AssetsManager();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();

    private final KvadratikCanvas canvas;
    private final KvadratikClient client;
    private final PhysicsWorker physics;
    private final ConsoleWorker console;
    private final Controls controls;

    private Level level;

    {
        addWindowListener(() -> {
            dispose();
            System.exit(0);
        });

        setTitle("kvadratik");
        applyIcon("assets/icon.png");

        // Multiplayer client
        client = new KvadratikClient(this, 50L);

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

        // Physics (100 heartbeats per second)
        physics = new PhysicsWorker(this, 50L);
        physics.start();
    }

    public void run() {
        join: {
            // Multiplayer client
            if (client.isInfoProvided()) {
                try {
                    client.start();
                    client.connect();
                    break join;
                } catch (UnknownHostException e1) {
                    Logger.error("Unknown host");
                } catch (IOException e1) {
                    Logger.error("Unable to join the server");
                }
            } else {
                Logger.info("Nothing provided for the client");
            }

            // Singleplayer
            String levelJson = ASSETS.textFile("assets/levels/whitespace.json");
            level = JSON.fromJsonLevel(this, levelJson, false);
        }
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

    public KvadratikCanvas getCanvas() {
        return canvas;
    }

    public KvadratikClient getClient() {
        return client;
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

    public void setLevel(Level level) {
        this.level = level;
    }

    public void addKeysListener(Canvas c) {
        c.addKeyListener(controls);
    }

    public void addWindowListener(Runnable handler) {
        WindowAdapter adapter = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                handler.run();
            }

        };
        addWindowListener(adapter);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            adapter.windowClosed(null);
        }));
    }

}
