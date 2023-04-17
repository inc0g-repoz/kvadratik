package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.client.Session;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RichPresenceWorker extends Worker {

    private final KvadratikGame game;
    private final DiscordRPC rpc;
    private final DiscordRichPresence presence;

    public RichPresenceWorker(KvadratikGame game) {
        super(2000L);
        this.game = game;

        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
        String appId = "1046333931106078721";

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = user -> Logger.info("Discord RPS: ready");
        rpc.Discord_Initialize(appId, handlers, true, "");

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.endTimestamp   = 0;
        presence.largeImageKey  = "logo";
        presence.details  = "";
        presence.state    = "Main Menu";
        presence.partyMax = 10;
        rpc.Discord_UpdatePresence(presence);
    }

    @Override
    protected void work() {
        if (!Thread.currentThread().isInterrupted()) {
            Session session = game.getSession();
            if (session == null) {
                String state = "Main Menu";
                if (!presence.state.equals(state)) {
                    presence.partySize = 0;
                    presence.state = state;
                    rpc.Discord_UpdatePresence(presence);
                }
            } else if (game.getClient().isConnected()) {
                Level level = game.getSession().getLevel();
                if (level != null) {
                    int size = (int) level.getBeings().stream().filter(Being::hasName).count();
                    if (size != presence.partySize) {
                        presence.partySize = size;
                        presence.state     = "Multiplayer";
                        rpc.Discord_UpdatePresence(presence);
                    }
                }
            } else {
                String state = "Singleplayer";
                if (!presence.state.equals(state)) {
                    presence.partySize = 0;
                    presence.state = state;
                    rpc.Discord_UpdatePresence(presence);
                }
            }

            rpc.Discord_RunCallbacks();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                rpc.Discord_Shutdown();
                kill();
            }
        }
    }
}
