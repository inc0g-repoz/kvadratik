package com.github.inc0grepoz.kvad.worker;

import java.util.Scanner;
import java.util.StringJoiner;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.Logger;

public class ConsoleWorker extends Worker {

    private final KvadratikServer kvad;
    private final String[] commandList = new String[] {
            "help",
            "log_packets",
            "stats",
            "stop"
    };

    private Scanner scan = new Scanner(System.in);
    private boolean logKeys;

    public ConsoleWorker(KvadratikServer kvad, long delay) {
        super(delay);
        this.kvad = kvad;
    }

    @Override
    protected void work() {
        String command = scan.nextLine().toLowerCase();

        switch (command) {
            case "help": {
                Logger.info("Useful commands:\n" + String.join("\n", commandList));
                return;
            }
            case "log_packets": {
                Packet.logging = !Packet.logging;
                Logger.info("Set packets logging to " + Boolean.toString(Packet.logging));
                return;
            }
            case "stats": {
                Level level = kvad.getLevel();
                StringJoiner sjStats = new StringJoiner("\n");
                sjStats.add("Stats:");
                sjStats.add("Beings: " + level.getBeings().size());
                sjStats.add("Objects: " + level.getLevelObjects().size());
                sjStats.add("Players: " + kvad.players.size());
                sjStats.add("Connections: " + kvad.connections.size());
                Logger.info(sjStats.toString());
                return;
            }
            case "stop": {
                Logger.info("Stopping the server");
                kvad.stop();
                return;
            }
        }

        Logger.error("Get some help");
    }

    @Override
    public void kill() {
        scan.close();
        super.kill();
    }

    public boolean isLoggingKeys() {
        return logKeys;
    }

}
