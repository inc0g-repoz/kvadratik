package com.github.inc0grepoz.kvad;

import java.util.Scanner;

import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;

public class ConsoleWorker extends Worker {

    private final KvadratikGame game;
    private final String[] commandList = new String[] {
            "cam_follow",
            "cam_free",
            "log_keys"
            
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
        String command = scan.next();

        switch (command) {
            case "help":
                System.out.println(String.join("\n", commandList));
                break;
            case "cam_follow":
                game.getLevel().getCamera().setMode(CameraMode.FOLLOW);
                System.out.println("Switched to default camera");
                break;
            case "cam_free":
                game.getLevel().getCamera().setMode(CameraMode.FREE);
                System.out.println("Switched to freecam");
                break;
            case "log_keys":
                logKeys = !logKeys;
                System.out.println("Set logging keys to " + Boolean.toString(logKeys));
                break;
        }
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
