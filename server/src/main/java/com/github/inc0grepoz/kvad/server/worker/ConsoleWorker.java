package com.github.inc0grepoz.kvad.server.worker;

import java.util.Scanner;
import java.util.StringJoiner;

import com.github.inc0grepoz.kvad.common.utils.Logger;
import com.github.inc0grepoz.kvad.common.worker.Worker;
import com.github.inc0grepoz.kvad.server.protocol.Packet;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

public class ConsoleWorker extends Worker {

    private final KvadratikServer kvad;
    private final String[] commandList = new String[] {
            "help",
            "kick",
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
        String command = scan.nextLine();

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
            case "scripts": {
                StringJoiner sjScripts = new StringJoiner(", ");
                kvad.scripts.getScripts().forEach(s -> sjScripts.add(s.getName()));
                Logger.info(kvad.scripts.getScripts().size() + " scripts: " + sjScripts.toString());
                return;
            }
            case "stats": {
                StringJoiner sjStats = new StringJoiner("\n");
                sjStats.add("Players: " + kvad.players.size());
                sjStats.add("Sockets: " + kvad.connections.size());
                kvad.levels.forEach(level -> {
                    sjStats.add(level.getName() + " stats:");
                    sjStats.add("- beings: " + level.getBeings().size());
                    sjStats.add("- objects: " + level.getLevelObjects().size());
                });
                Logger.info(sjStats.toString());
                return;
            }
            case "stop": {
                kvad.stop();
                return;
            }
        }

        if (command.startsWith("kick ")) {
            String name = command.substring(5);
            try {
                kvad.getPlayerByName(name).disconnect();
                Logger.info("Kicked " + name);
            } catch (Exception nfe) {
                Logger.error(name + " not found");
            }
            return;
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
