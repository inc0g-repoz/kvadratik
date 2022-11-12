package com.github.inc0grepoz.kvad.protocol;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingType;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.XML;

public class PacketUtil {

    private final KvadratikGame game;

    public PacketUtil(KvadratikGame game) {
        this.game = game;
    }

    public void createBeing(Packet packet) {
        if (packet.type() != PacketType.SERVER_BEING_SPAWN) {
            Logger.error("Unable to init a being from " + packet.type().name());
        }

        Map<String, String> map = packet.toMap();

        // Getting a server-side unique ID
        String uidStr = map.getOrDefault("uid", null);
        UUID uid = uidStr == null ? null : UUID.fromString(uidStr);

        // Looking for the same entity
        Level level = game.getLevel();
        boolean hasOne = level.getBeings().stream()
                .anyMatch(b -> b.getUniqueId().equals(uid));
        if (hasOne) {
            return;
        }

        // Checking if the level is an appropriate one
        String srvLevel = map.get("level");
        if (!level.getName().equals(srvLevel)) {
            return;
        }

        // Building a rectangle array
        Builder b = IntStream.builder();
        Stream.of(map.get("rect").split(",")).forEach(s -> {
            b.add(Integer.valueOf(s).intValue());
        });

        // Looking for the client-side being type
        BeingType type = BeingType.valueOf(map.get("type"));

        Being being = new Being(b.build().toArray(), level, type, uid);
        level.getBeings().add(being);
    }

    public void buildLevel(Packet packet) {
        if (packet.type() != PacketType.SERVER_LEVEL) {
            Logger.error("Unable to build a level from " + packet.type().name());
        }
        Level level = new Level(game, XML.fromString(packet.toString()));
        game.setLevel(level);
    }

}
