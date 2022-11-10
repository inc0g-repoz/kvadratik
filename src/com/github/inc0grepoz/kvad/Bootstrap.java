package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.client.KvadratikClient;

public class Bootstrap {

    public static void main(String[] args) {
        KvadratikGame game = new KvadratikGame();
        game.setSize(640, 480);
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        game.setVisible(true);

        KvadratikClient client = game.getClient();
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("=")) {
                String[] split = args[i].split("=");
                switch (split[0]) {
                    case "nickname": {
                        client.setNickname(split[1]);
                        break;
                    }
                    case "server": {
                        String[] server = split[1].split(":");
                        client.setServerIp(server[0]);
                        client.setServerPort(Integer.valueOf(server[1]));
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

}
