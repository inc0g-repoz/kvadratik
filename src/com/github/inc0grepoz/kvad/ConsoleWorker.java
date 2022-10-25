package com.github.inc0grepoz.kvad;

import java.util.Scanner;

import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;

public class ConsoleWorker extends Worker {

    private final KvadratikGame game;
    private final String[] commandList = new String[] {
            "cam_free",
            "cam_follow"
    };

    private Scanner scan;

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
            case "cam_free":
                game.getLevel().getCamera().setMode(CameraMode.FREE);
                System.out.println("Switched to freecam");
                break;
            case "cam_follow":
                game.getLevel().getCamera().setMode(CameraMode.FOLLOW);
                System.out.println("Switched to default camera");
                break;
        }
    }

    @Override
    public void kill() {
        scan.close();
        super.kill();
    }

}
