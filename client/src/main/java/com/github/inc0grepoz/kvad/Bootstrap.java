package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.RichPresenceWorker;

public class Bootstrap {

    static {
        KvadratikGame.INSTANCE.setTitle("kvadratik");
        KvadratikGame.INSTANCE.applyIcon("assets/icon.png");
        KvadratikGame.INSTANCE.setSize(640, 480);
        KvadratikGame.INSTANCE.setResizable(true);
        KvadratikGame.INSTANCE.setLocationRelativeTo(null);
//      KvadratikGame.INSTANCE.setUndecorated(true);
        KvadratikGame.INSTANCE.setVisible(true);
    }

    public static void main(String[] args) {
        KvadratikClient client = KvadratikGame.INSTANCE.getClient();

        // Reading the launch arguments
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

        KvadratikGame.INSTANCE.getCanvas().mainMenu();
        RichPresenceWorker rpw = new RichPresenceWorker(KvadratikGame.INSTANCE);
        rpw.start();
    }

}
