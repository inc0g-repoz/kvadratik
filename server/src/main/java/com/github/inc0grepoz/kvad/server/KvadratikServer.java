package com.github.inc0grepoz.kvad.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PacketHandler;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;
import com.github.inc0grepoz.kvad.worker.SocketAcceptor;
import com.github.inc0grepoz.kvad.worker.Worker;

public class KvadratikServer implements Kvadratik {

    public static final AssetsProvider ASSETS = new AssetsProvider();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();
    public static final KvadratikServer INSTANCE = new KvadratikServer();

    public final List<Level> levels = new ArrayList<>();
    public final List<Player> players = new ArrayList<>();
    public final List<Connection> connections = new ArrayList<>();
    public final PacketUtil packetUtil = new PacketUtil(this);
    public final Settings settings;

    private final List<Worker> workers = new ArrayList<>();
    private final PlayerCommandHandler commandHandler = new PlayerCommandHandler(this);

    {
        if (INSTANCE != null) {
            throw new IllegalStateException("Server instance already has been initialized");
        }

        // Creating the server levels
        ASSETS.listFiles("assets/levels")
                .filter(fn -> fn.endsWith(".json"))
                .forEach(path -> {
                    String levelJson = ASSETS.textFile(path);
                    levels.add(JSON.fromJsonLevel(this, levelJson));
        });

        // Loading the server settings
        String settingsJson = ASSETS.textFile("settings.json");
        settings = JSON.fromJsonSettings(settingsJson);

        workers.add(new ConsoleWorker(this, 500));
        workers.add(new PacketHandler(this));
        workers.add(new PhysicsWorker(this, 50));
        workers.add(new SocketAcceptor(this, settings.port));
    }

    public Level getLevelByName(String name) {
        return levels.stream().filter(level -> level.getName().equals(name))
                .findFirst().orElse(null);
    }

    public void handlePlayerCommand(Player player, String command) {
        commandHandler.execute(player, command);
    }

    public void closeExpiredConnections() {
        Iterator<Connection> eachCxn = connections.iterator();
        while (eachCxn.hasNext()) {
            Connection cxn = eachCxn.next();
            if (cxn.hasExpired()) {
                Iterator<Player> eachFromPList = players.iterator();

                // Despawning the disconnected player for others
                while (eachFromPList.hasNext()) {
                    Player p = eachFromPList.next();
                    if (p.getConnection().equals(cxn)) {
                        Logger.info(p.getName() + " left the server");
                        packetUtil.outBeingDespawn(p);
                        p.delete();
                        eachFromPList.remove();
                    }
                }

                // Remove it from the playerlist
                eachCxn.remove();

                // Closing the connection
                try {
                    cxn.close();
                } catch (IOException e) {
                    Logger.error("Unable to close a player connection");
                }
            }
        }
    }

    public void start() {
        workers.forEach(Worker::start);
    }

    public void stop() {
        Logger.info("Stopping the server");
        workers.forEach(Worker::kill);
        System.exit(0);
    }

}
