package com.github.inc0grepoz.kvad.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class KvadratikGame extends Frame {

    public static final AssetsManager ASSETS = new AssetsManager();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();

    private final @Getter Controls controls;
    private final @Getter KvadratikCanvas canvas;
    private final @Getter KvadratikClient client;
    private final @Getter ConsoleWorker console;
    private final PhysicsWorker physics;

    private @Getter @Setter Level level;

    {
        addShutdownListener(() -> {
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

        // Physics
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

                    Message message = new Message();
                    message.addComponent("Unknown host.", Color.RED);
                    client.getChat().print(message);
                } catch (IOException e1) {
                    Logger.error("Unable to join the server");

                    Message message = new Message();
                    message.addComponent("Unable to join the server.", Color.RED);
                    client.getChat().print(message);
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

    public void addKeysListener(Canvas c) {
        c.addKeyListener(controls);
    }

    public void addShutdownListener(Runnable handler) {
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
