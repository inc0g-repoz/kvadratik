package com.github.inc0grepoz.kvad;

import java.util.Scanner;
import java.util.stream.Stream;

import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.utils.Logger;

public class ConsoleWorker extends Worker {

    private final KvadratikGame game;
    private final String[] commandList = new String[] {
            "cam_follow",
            "cam_free",
            "draw_colliders",
            "fps",
            "help",
            "log_keys",
            "teleport",
            "view_misc"
    };

    private Scanner scan;
    private boolean logKeys;

    public ConsoleWorker(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
    }

    @Override
    public void work() {
        scan = new Scanner(System.in);
        String command = scan.nextLine().toLowerCase();
        KvadratikCanvas canvas = game.getCanvas();

        switch (command) {
            case "cam_follow":
                game.getLevel().getCamera().setMode(CameraMode.FOLLOW);
                Logger.info("Switched to default camera");
                return;
            case "cam_free":
                game.getLevel().getCamera().setMode(CameraMode.FREE);
                Logger.info("Switched to freecam");
                return;
            case "draw_colliders":
                boolean draw = !canvas.isDrawCollidersEnabled();
                canvas.setDrawColliders(draw);
                Logger.info("Colliders are " + (draw ? "shown" : "hidden"));
                return;
            case "help":
                Logger.info(String.join("\n", commandList));
                return;
            case "log_keys":
                logKeys = !logKeys;
                Logger.info("Set logging keys to " + Boolean.toString(logKeys));
                return;
            case "view_misc":
                canvas.setViewMiscInfo(!canvas.isMiscInfoViewed());
                Logger.info("Misc info is " + (canvas.isMiscInfoViewed() ? "shown" : "hidden"));
                return;
        }

        if (command.startsWith("fps ")) {
            try {
                int cap = Integer.valueOf(command.substring(4));
                game.getCanvas().setFrapsPerSecond(cap);
                Logger.info("Set FPS capability to " + cap);
            } catch (NumberFormatException nfe) {
                Logger.error("Invalid value");
            }
            return;
        }

        if (command.startsWith("teleport ")) {
            try {
                Integer[] args = Stream.of(command.substring(9).split(" "))
                        .map(Integer::valueOf).toArray(Integer[]::new);
                game.getLevel().getPlayer().teleport(args[0], args[1]);
                Logger.info("Teleported to [" + args[0] + "," + args[1] + "]");
            } catch (Exception e) {
                Logger.error("Invalid arguments");
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
