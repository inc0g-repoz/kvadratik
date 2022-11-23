package com.github.inc0grepoz.kvad.protocol;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingType;
import com.github.inc0grepoz.kvad.entities.chat.Message;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.Vector;
import com.github.inc0grepoz.kvad.utils.XML;

public class PacketUtil {

    private final KvadratikGame game;

    public PacketUtil(KvadratikGame game) {
        this.game = game;
    }

    public void outAnim() {
        if (game.getClient().isConnected()) {
            String name = game.getLevel().getPlayer().getAnim().name();
            PacketType.CLIENT_PLAYER_ANIM.create(name).queue(game.getClient());
        }
    }

    public void outRect(Being being, Vector prevMove, int x, int y) {
        if (!game.getClient().isConnected()) {
            return;
        }

        if (prevMove.x == x && prevMove.y == y) {
            return;
        }

        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append("x=");
        sb.append(rect.x);
        sb.append(";y=");
        sb.append(rect.y);
        sb.append(";w=");
        sb.append(rect.width);
        sb.append(";h=");
        sb.append(rect.height);
        PacketType.CLIENT_PLAYER_RECT.create(sb.toString()).queue(game.getClient());
    }

    public void inAnim(Packet packet) {
        if (packet.getType() != PacketType.SERVER_BEING_ANIM) {
            Logger.error("Unable to read an anim from " + packet.getType().name());
            return;
        }

        Map<String, String> map = packet.toMap();

        // Getting a server-side unique ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the client-side anim
        Anim anim = Anim.valueOf(map.get("anim"));

        // Looking for the desired entity
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            being.applyAnim(anim);
        }
    }

    public void inBeingDespawn(Packet packet) {
        int id = Integer.valueOf(packet.toString());
        game.getLevel().getBeings().removeIf(being -> {
            return being.getId() == id;
        });
    }

    public void inCreateBeing(Packet packet) {
        if (packet.getType() != PacketType.SERVER_BEING_SPAWN) {
            Logger.error("Unable to init a being from " + packet.getType().name());
            return;
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

    public void inLevel(Packet packet) {
        if (packet.getType() != PacketType.SERVER_LEVEL) {
            Logger.error("Unable to build a level from " + packet.getType().name());
            return;
        }
        Level level = new Level(game, XML.fromString(packet.toString()), false);
        game.setLevel(level);
    }

    public void inPlayerMessage(Packet packet) {
        if (packet.getType() != PacketType.SERVER_CHAT_MESSAGE) {
            Logger.error("Unable to read a chat message from " + packet.getType().name());
            return;
        }

        KvadratikClient client = game.getClient();
        if (!client.isConnected()) {
            return;
        }

        Map<String, String> map = packet.toMap(3);

        String colStr = map.get("color");
        String[] split = colStr.split(",");
        int[] rgb = {
                Integer.valueOf(split[0]),
                Integer.valueOf(split[1]),
                Integer.valueOf(split[2])
        };
        Color color = RGB.get(rgb);

        String name = map.get("name");
        String text = map.get("text");

        Message message = new Message();
        if (name != null && text != null) {
            message.addComponent(name + ": ", color).addComponent(text);
            client.getChat().print(message);
        }
    }

    public void inRect(Packet packet) {
        Map<String, String> map = packet.toMap();
        int id = Integer.valueOf(map.get("id"));
        int x = Integer.valueOf(map.get("x"));
        int y = Integer.valueOf(map.get("y"));
        int w = Integer.valueOf(map.get("w"));
        int h = Integer.valueOf(map.get("h"));

        // Looking for the desired entity
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            Rectangle rect = being.getRectangle();
            rect.x = x;
            rect.y = y;
            rect.width = w;
            rect.height = h;
        }
    }

}
