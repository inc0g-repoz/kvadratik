package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import com.github.inc0grepoz.commons.util.json.mapper.JsonMapper;
import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.gui.Message;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMouseListener;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMouseMotionListener;
import com.github.inc0grepoz.kvad.ksf.ScriptManager;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.Platform;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unchecked")
public class KvadratikGame extends Frame implements Kvadratik {

    public static final JsonMapper JSON_MAPPER = new JsonMapper();
    public static final AssetsProvider ASSETS = new AssetsProvider();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();
    public static final KvadratikGame INSTANCE = Platform.init(new KvadratikGame());

    private static final long serialVersionUID = 3533037507759276338L;

    private final @Getter Controls controls;
    private final @Getter KvadratikCanvas canvas;
    private final @Getter KvadratikClient client;
    private final @Getter ConsoleWorker console;

    private @Getter ScriptManager scripts = new ScriptManager(this);
    private @Getter @Setter Session session;

    {
        if (INSTANCE != null) {
            throw new IllegalStateException("Game instance already has been initialized");
        }

        // Multiplayer client
        client = new KvadratikClient(this, 50L);

        addShutdownListener(() -> {
            client.disconnect(); // If needed
            dispose();
            System.exit(0);
        });

        // Compiling scripts
        scripts.loadScripts();

        // Copying and loading settings
        ASSETS.copy("settings.json");
        Map<String, String> settings = JSON_MAPPER.deserialize(ASSETS.textFile("settings.json"), Map.class, String.class, String.class);
        if (!client.isInfoProvided()) {
            client.setNickname(settings.getOrDefault("username", "DummyName"));
        }

        // Rendering
        canvas = new KvadratikCanvas(this, getWidth(), getHeight());
        canvas.setBackground(Color.BLACK);
        canvas.addMouseListener(new CanvasMouseListener(canvas));
        canvas.addMouseMotionListener(new CanvasMouseMotionListener(canvas));
        add(canvas);

        // Controls
        controls = new Controls(this);
        addKeyListener(controls);

        // Debug console
        console = new ConsoleWorker(this, 500L);
        console.start();
    }

    @Override
    public AssetsProvider getAssetsProvider() {
        return ASSETS;
    }

    @Override
    public JsonMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    @Override
    public BeingFactory getBeingFactory() {
        return BEING_FACTORY;
    }

    @Override
    public LevelObjectFactory getLevelObjectFactory() {
        return OBJECT_FACTORY;
    }

    @Override
    public void move(Being being, double dx, double dy) {
        client.getPacketUtil().outPoint(being, dx, dy);
    }

    @Override
    public void sendAnim(Being being) {
        client.getPacketUtil().outAnim();
    }

    @Override
    public boolean isDrawingColliders() {
        return canvas.drawColliders;
    }

    public void run() {
        loadLevel("assets/levels/default.json");
    }

    public void loadLevel(String path) {
        canvas.setMenu(null);
        client.disconnect(); // If needed
        session = Session.loadLevel(path);
    }

    public void joinServer(String host) {
        canvas.setMenu(null);
        try {
            String[] ip = host.split(":");
            client.setHost(ip[0]);
            client.setPort(ip.length > 1 ? Integer.valueOf(ip[1]) : 30405);

            if (!client.isInfoProvided()) {
                Logger.error("Nickname provided in settings.json appears invalid");

                Message message = new Message();
                message.addComponent("Nickname provided in settings.json appears invalid.", Color.RED);
                client.getChat().print(message);
                return;
            }

            client.start();
            client.connect();
        } catch (UnknownHostException e) {
            Logger.error("Unknown host");

            Message message = new Message();
            message.addComponent("Unknown host.", Color.RED);
            client.getChat().print(message);
        } catch (IOException e) {
            Logger.error("Unable to join the server");

            Message message = new Message();
            message.addComponent("Unable to join the server.", Color.RED);
            client.getChat().print(message);
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
