package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.utils.Logger;

import com.github.inc0grepoz.kvad.worker.RichPresenceWorker;

public class Bootstrap {

    private static final KvadratikGame GAME = new KvadratikGame();

    static {
        GAME.setSize(640, 480);
        GAME.setResizable(false);
        GAME.setLocationRelativeTo(null);
        GAME.setVisible(true);
    }

    public static void main(String[] args) {
        KvadratikClient client = GAME.getClient();
        for (int i = 0; i < args.length; i++) {
            Logger.info("Reading launch args: " + args[i]);
            if (args[i].contains("=")) {
                String[] split = args[i].split("=");
                switch (split[0]) {
                    case "nickname": {
                        client.setNickname(split[1]);
                        break;
                    }
                    case "server": {
                        String[] server = split[1].split(":");
                        client.setHost(server[0]);
                        client.setPort(Integer.parseInt(server[1]));
                        break;
                    }
                    default:
                }
            }
        }

        GAME.run();
        RichPresenceWorker rpw = new RichPresenceWorker(GAME);
        rpw.start();
    }

}
