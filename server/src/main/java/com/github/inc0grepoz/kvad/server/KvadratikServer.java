package com.github.inc0grepoz.kvad.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.ksf.Script;
import com.github.inc0grepoz.kvad.ksf.VarPool;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PacketHandler;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;
import com.github.inc0grepoz.kvad.worker.SocketAcceptor;
import com.github.inc0grepoz.kvad.worker.Worker;

public class KvadratikServer {

    public static final AssetsProvider ASSETS = new AssetsProvider();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();
    public static final KvadratikServer INSTANCE = new KvadratikServer();

    public final List<Level> levels = new ArrayList<>();
    public final List<Player> players = new ArrayList<>();
    public final List<Connection> connections = new ArrayList<>();
    public final List<Script> scripts;

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

        // Compiling scripts
        VarPool vars = new VarPool();
        vars.declare("kvad", this);
        scripts = ASSETS.scripts("scripts", vars);

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

    public Player getPlayerByName(String name) {
        return players.stream().filter(player -> player.getName().equals(name))
                .findFirst().orElse(null);
    }

    public void handlePlayerCommand(Player player, String command) {
        commandHandler.execute(player, command);
    }

    public Player unbindPlayer(String name) {
        Player player = getPlayerByName(name);
        if (player != null) {
            player.delete();
            return player;
        }
        return null;
    }

    public void closeExpiredConnections() {
        Iterator<Connection> eachCxn = connections.iterator();
        while (eachCxn.hasNext()) {
            Connection cxn = eachCxn.next();
            if (cxn.hasExpired()) {
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

        // Despawning the disconnected players
        Iterator<Player> eachPlayer = players.iterator();
        while (eachPlayer.hasNext()) {
            Player p = eachPlayer.next();
            if (p.getConnection() == null || p.getConnection().hasExpired()) {
                Logger.info(p.getName() + " left the server");
                packetUtil.outBeingDespawn(p);

                eachPlayer.remove();
                p.delete();
            }
        }
    }

    public void run() {
        workers.forEach(Worker::start);
    }

    public void stop() {
        Logger.info("Stopping the server");
        workers.forEach(Worker::kill);
        System.exit(0);
    }

}
