package com.github.inc0grepoz.kvad.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.inc0grepoz.kvad.entities.BeingFactory;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.ConsoleWorker;
import com.github.inc0grepoz.kvad.worker.PacketHandler;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;
import com.github.inc0grepoz.kvad.worker.SocketAcceptor;
import com.github.inc0grepoz.kvad.worker.Worker;

public class KvadratikServer {

    public static final AssetsManager ASSETS = new AssetsManager();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();

    public static AssetsManager getAssets() {
        return ASSETS;
    }   

    private final PacketUtil packetUtil = new PacketUtil(this);
    private final PlayerCommandHandler commandHandler = new PlayerCommandHandler(this);
    private final List<Worker> workers = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private final int connectionTimeout = 10000;

    private Level level;

    public KvadratikServer() {
        workers.add(new ConsoleWorker(this, 500));
        workers.add(new PacketHandler(this));
        workers.add(new PhysicsWorker(this, 50));
        workers.add(new SocketAcceptor(this, 30405));

        // Creating the server level
        String levelJson = ASSETS.textFile("assets/levels/whitespace.json");
        level = JSON.fromJsonLevel(this, levelJson);
    }

    public void logPlayerIn(String name, Connection connection) {
        boolean already = players.stream().anyMatch(player -> {
            return connection.equals(player.getConnection());
        });
        if (!already) {
            Player player = level.getPlayerPreset().spawn(connection, name, level);

            // Sending the level data and all beings
            packetUtil.outLevel(player);
            packetUtil.outSpawnBeingForAll(player);
            level.getBeings().forEach(being -> packetUtil.outSpawnBeing(player, being));

            packetUtil.outTransferControl(player, player);

            String ip = connection.getInetAddress().getHostAddress();
            Logger.info(name + " joined the server from " + ip);
        }
    }

    public PacketUtil getPacketUtil() {
        return packetUtil;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Level getLevel() {
        return level;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void handlePlayerCommand(Player player, String command) {
        commandHandler.execute(player, command);
    }

    public void closeExpiredConnections() {
        Iterator<Connection> eachCxn = connections.iterator();
        while (eachCxn.hasNext()) {
            Connection cxn = eachCxn.next();
            if (cxn.hasExpired()) {
                Iterator<Player> eachP = players.iterator();

                // Despawning the disconnected player for others
                while (eachP.hasNext()) {
                    Player p = eachP.next();
                    if (p.getConnection().equals(cxn)) {
                        Logger.info(p.getName() + " left the server");
                        packetUtil.outDespawnBeing(p);
                        level.getBeings().remove(p);
                        eachP.remove();
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
        workers.forEach(Worker::kill);
        System.exit(0);
    }

}
