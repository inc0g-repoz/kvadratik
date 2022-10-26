package com.github.inc0grepoz.kvad;

import java.util.Scanner;

import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;

public class ConsoleWorker extends Worker {

    private final KvadratikGame game;
    private final String[] commandList = new String[] {
            "cam_follow",
            "cam_free",
            "help",
            "log_keys",
            "show_fps"
            
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

        switch (command) {
            case "help":
                System.out.println(String.join("\n", commandList));
                return;
            case "cam_follow":
                game.getLevel().getCamera().setMode(CameraMode.FOLLOW);
                System.out.println("Switched to default camera");
                return;
            case "cam_free":
                game.getLevel().getCamera().setMode(CameraMode.FREE);
                System.out.println("Switched to freecam");
                return;
            case "log_keys":
                logKeys = !logKeys;
                System.out.println("Set logging keys to " + Boolean.toString(logKeys));
                return;
            case "show_fps":
                FrapsCounter fps = game.getCanvas().getFrapsCounter();
                fps.setEnabled(!fps.isEnabled());
                System.out.println("FPS count is " + (fps.isEnabled() ? "shown" : "hidden"));
                return;
        }

        if (command.startsWith("fps ")) {
            try {
                int cap = Integer.valueOf(command.substring(4));
                game.getCanvas().setFrapsPerSecond(cap);
                System.out.println("Set FPS capability to " + cap);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid value");
            }
            return;
        }

        System.out.println("Get some help");
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
