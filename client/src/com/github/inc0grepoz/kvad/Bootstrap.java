package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

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
                        client.setServerIp(server[0]);
                        client.setServerPort(Integer.valueOf(server[1]));
                        break;
                    }
                    default:
                }
            }
        }

        GAME.run();
        initDiscordRPS();
    }

    public static void initDiscordRPS() {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordRichPresence presence = new DiscordRichPresence();
        String appId = "1046333931106078721";

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = user -> Logger.info("Discord RPS: ready");
        lib.Discord_Initialize(appId, handlers, true, "");

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.endTimestamp   = 0;
        presence.largeImageKey  = "logo";
        presence.details   = "";
        presence.state     = "Singleplayer";
        presence.partyMax  = 10;
        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (GAME.getClient().isConnected()) {
                    Level level = GAME.getLevel();
                    if (level != null) {
                        int size = GAME.getLevel().getBeings().size();
                        if (size != presence.partySize) {
                            presence.partySize = GAME.getLevel().getBeings().size();
                            presence.state     = "Multiplayer";
                            lib.Discord_UpdatePresence(presence);
                        }
                    }
                }

                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        t.start();
    }

}
