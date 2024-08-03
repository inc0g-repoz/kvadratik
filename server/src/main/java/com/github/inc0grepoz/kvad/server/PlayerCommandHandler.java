package com.github.inc0grepoz.kvad.server;

import java.awt.Color;
import java.util.Arrays;

import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerCommandHandler {

    private final KvadratikServer kvad;

    public void execute(Player player, String command) {
        command = command.substring(1);
        String[] split = command.split(" ");

        switch (split[0]) {
            case "morph": {
                if (split.length == 2 && KvadratikServer.BEING_FACTORY.hasType(split[1])) {
                    player.morph(split[1]);
                    kvad.packetUtil.outBeingType(player);
                }
                break;
            }
            case "goto": {
                if (split.length > 1) {
                    String levelName = String.join(" ", Arrays.copyOfRange(split, 1, split.length));
                    Level level = kvad.levels.stream()
                            .filter(l -> l.getName().equals(levelName))
                            .findFirst().orElse(null);
                    if (level != null) {
                        level.join(player.getConnection(), level, player.getName());
                    } else {
                        Message msg = new Message();
                        msg.addComponent("Unknown level `" + levelName + "`", Color.RED);
                        player.sendMessage(msg);
                    }
                }
                break;
            }
            default:
        }
    }

}
