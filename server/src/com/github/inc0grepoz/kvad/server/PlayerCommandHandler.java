package com.github.inc0grepoz.kvad.server;

import com.github.inc0grepoz.kvad.entities.being.Player;

public class PlayerCommandHandler {

    private final KvadratikServer kvad;

    public PlayerCommandHandler(KvadratikServer kvad) {
        this.kvad = kvad;
    }

    public void execute(Player player, String command) {
        command = command.substring(1);
        String[] split = command.contains(" ") ? command.split(" ")
                : new String[] { command };

        switch (split[0]) {
            case "morph": {
                if (split.length == 2 && KvadratikServer.BEING_FACTORY.hasType(split[1])) {
                    player.morph(split[1]);
                    kvad.getPacketUtil().outBeingType(player);
                }
                break;
            }
            default:
        }
    }

}
