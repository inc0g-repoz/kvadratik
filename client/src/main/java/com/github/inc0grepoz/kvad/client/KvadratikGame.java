package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.client.awt.CanvasMouseListener;
import com.github.inc0grepoz.kvad.client.awt.CanvasMouseMotionListener;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.gui.Message;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;

import lombok.Getter;
import lombok.Setter;

public class KvadratikGame extends Frame implements Kvadratik {

    public static final AssetsProvider ASSETS = new AssetsProvider();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();
    public static final KvadratikGame INSTANCE = new KvadratikGame();

    private static final long serialVersionUID = 3533037507759276338L;

    private final @Getter Controls controls;
    private final @Getter KvadratikCanvas canvas;
    private final @Getter KvadratikClient client;
    private final @Getter ConsoleWorker console;

    private @Getter @Setter Session session;

    {
        if (INSTANCE != null) {
            throw new IllegalStateException("Game instance already has been initialized");
        }

        // Multiplayer client
        client = new KvadratikClient(this, 50L);

        addShutdownListener(() -> {
            if (client.isConnected()) {
                Packet.out(PacketType.CLIENT_DISCONNECT, " ").queue(client);
                client.flushOut();
            }

            dispose();
            System.exit(0);
        });

        // Rendering
        canvas = new KvadratikCanvas(this, getWidth(), getHeight());
        canvas.setBackground(Color.BLACK);
        add(canvas);
        canvas.setFrapsPerSecond(60);
        canvas.addMouseListener(new CanvasMouseListener(canvas));
        canvas.addMouseMotionListener(new CanvasMouseMotionListener(canvas));
        canvas.getWorker().start();

        // Controls
        controls = new Controls(this);
        canvas.addKeyListener(controls);

        // Debug console
        console = new ConsoleWorker(this, 500L);
        console.start();
    }

    @Override
    public void run() {
        loadLevel("assets/levels/whitespace.json");
    }

    public void loadLevel(String path) {
        session = Session.loadLevel(path);
    }

    public void joinServer(String host) {
        if (client.isInfoProvided()) {
            try {
                String[] ip = host.split(":");
                client.setHost(ip[0]);
                client.setPort(ip.length > 1 ? Integer.valueOf(ip[1]) : 30405);
                client.start();
                client.connect();
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
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

    private void addShutdownListener(Runnable handler) {
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
