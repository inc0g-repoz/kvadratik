package com.github.inc0grepoz.kvad.protocol;

import java.awt.Rectangle;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.client.KvadratikGame;
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

    public void anim() {
        if (game.getClient().isConnected()) {
            String name = game.getLevel().getPlayer().getAnim().name();
            PacketType.CLIENT_PLAYER_ANIM.create(name).queue(game.getClient());
        }
    }

    public void speed(int x, int y) {
        PacketType.CLIENT_PLAYER_SPEED.create(x + "," + y).queue(game.getClient());
    }

    public void rect(Being being) {
        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append(rect.x);
        sb.append(",");
        sb.append(rect.y);
        sb.append(",");
        sb.append(rect.width);
        sb.append(",");
        sb.append(rect.height);
        PacketType.CLIENT_PLAYER_RECT.create(sb.toString()).queue(game.getClient());
    }

    public void rectThenSpeed(Being being, int x, int y) {
        if (game.getClient().isConnected()) {
            rect(being);
            speed(x, y);
        }
    }

    public void createBeing(Packet packet) {
        if (packet.getType() != PacketType.SERVER_BEING_SPAWN) {
            Logger.error("Unable to init a being from " + packet.getType().name());
        }

        Map<String, String> map = packet.toMap();

        // Getting a server-side unique ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the same entity
        Level level = game.getLevel();
        boolean hasOne = level.getBeings().stream()
                .anyMatch(b -> b.getId() == id);
        if (hasOne) {
            Logger.error("Tried to spawn a being with an invalid ID");
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
        int[] rect = b.build().toArray();

        // Looking for the client-side being type
        BeingType type = BeingType.valueOf(map.get("type"));

        Being being = new Being(level, rect, type, id);
        String name = map.getOrDefault("name", null);
        being.setName(name);
        level.getBeings().add(being);
    }

    public void buildLevel(Packet packet) {
        if (packet.getType() != PacketType.SERVER_LEVEL) {
            Logger.error("Unable to build a level from " + packet.getType().name());
        }
        Level level = new Level(game, XML.fromString(packet.toString()), false);
        game.setLevel(level);
    }

}
