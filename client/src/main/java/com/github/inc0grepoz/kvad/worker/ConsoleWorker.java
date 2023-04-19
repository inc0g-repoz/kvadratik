package com.github.inc0grepoz.kvad.worker;

import java.util.Scanner;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.client.Session;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.utils.Logger;

public class ConsoleWorker extends Worker {

    private final KvadratikGame game;
    private final String[] commandList = new String[] {
            "cam_follow",
            "cam_free",
            "connect",
            "draw_colliders",
            "fps",
            "help",
            "level",
            "log_keys",
            "log_packets",
            "teleport",
            "view_misc",
            "walk_speed"
    };

    private Scanner scan = new Scanner(System.in);
    private boolean logKeys;

    public ConsoleWorker(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
    }

    @Override
    protected void work() {
        String command = scan.nextLine().toLowerCase();
        KvadratikCanvas canvas = game.getCanvas();

        switch (command) {
            case "cam_follow":
                game.getSession().getLevel().getCamera().mode = CameraMode.FOLLOW;
                Logger.info("Switched to default camera");
                return;
            case "cam_free":
                game.getSession().getLevel().getCamera().mode = CameraMode.FREE;
                Logger.info("Switched to freecam");
                return;
            case "draw_colliders":
                boolean draw = (canvas.drawColliders = !canvas.drawColliders);
                Logger.info("Colliders are " + (draw ? "shown" : "hidden"));
                return;
            case "help":
                Logger.info("Useful commands:\n" + String.join("\n", commandList));
                return;
            case "log_keys":
                logKeys = !logKeys;
                Logger.info("Set keys logging to " + Boolean.toString(logKeys));
                return;
            case "log_packets":
                Packet.logging = !Packet.logging;
                Logger.info("Set packets logging to " + Boolean.toString(Packet.logging));
                return;
            case "view_misc":
                boolean view = (canvas.miscInfo = !canvas.miscInfo);
                Logger.info("Misc info is " + (view ? "shown" : "hidden"));
                return;
        }

        if (command.startsWith("connect ")) {
            String ip = command.substring(8);
            game.joinServer(ip);
            return;
        } else if (command.startsWith("fps ")) {
            try {
                int cap = Integer.valueOf(command.substring(4));
                game.getCanvas().setFrapsPerSecond(cap);
                Logger.info("Set FPS capability to " + cap);
            } catch (NumberFormatException nfe) {
                Logger.error("Invalid value");
            }
            return;
        } else if (command.startsWith("level ")) {
            try {
                String level = command.substring(6);
                game.setSession(Session.loadLevel(level));
            } catch (Throwable t) {
                Logger.error("Invalid level path");
            }
            return;
        } else if (command.startsWith("teleport ")) {
            try {
                Integer[] args = Stream.of(command.substring(9).split(" "))
                        .map(Integer::valueOf).toArray(Integer[]::new);
                game.getSession().getLevel().getPlayer().teleport(args[0], args[1]);
                Logger.info("Teleported to [" + args[0] + "," + args[1] + "]");
            } catch (Exception e) {
                Logger.error("Invalid arguments");
            }
            return;
        } else if (command.startsWith("walk_speed ")) {
            try {
                int speed = Integer.valueOf(command.substring(11));
                game.getSession().getLevel().getPlayer().speed = speed;
                Logger.info("Set FPS walking speed to " + speed);
            } catch (NumberFormatException nfe) {
                Logger.error("Invalid value");
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
