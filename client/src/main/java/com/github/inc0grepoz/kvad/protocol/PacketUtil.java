package com.github.inc0grepoz.kvad.protocol;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Downloader;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.Unzipper;
import com.github.inc0grepoz.kvad.utils.Vector;

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

    public void outPoint(Being being, Vector prevMove, int x, int y) {
        if (!game.getClient().isConnected()
                || being.getId() != game.getLevel().getPlayer().getId()
                || prevMove.x == x && prevMove.y == y) {
            return;
        }

        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append("x=");
        sb.append(rect.x);
        sb.append(";y=");
        sb.append(rect.y);
        PacketType.CLIENT_PLAYER_POINT.create(sb.toString()).queue(game.getClient());
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

    public void inAssets(Packet packet) {
        String strUrl = packet.toString();
        try {
            URL url = new URL(strUrl);

            Logger.info("Measuring the assets zip-file size");
            int size = Downloader.getFileSize(url);

            String fileName = UUID.nameUUIDFromBytes((url + ":" + size).getBytes())
                    .toString().replace("-", "");

            File assetsDir = new File("server-assets/" + fileName);
            if (assetsDir.exists() && assetsDir.isDirectory()) {
                Logger.info("Found an up-to-date version of the assets-pack");
            } else {
                File assetsZip = new File(assetsDir, fileName.concat(".zip"));
                Logger.info("Downloading the assets from " + strUrl);
                Downloader.download(url, assetsZip);
                Logger.info("Extracting the assets");
                Unzipper.unzip(assetsZip, assetsDir);
                assetsZip.delete();
            }
            KvadratikGame.ASSETS.assetsParent = assetsDir.toString();
        } catch (IOException e) {
            Logger.error("Invalid assets URL");
        }
    }

    public void inBeingDespawn(Packet packet) {
        int id = Integer.valueOf(packet.toString());
        game.getLevel().getBeings().removeIf(being -> {
            return being.getId() == id;
        });
    }

    public void inBeingSpawn(Packet packet) {
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

        // Reading the point
        String[] pArr = map.get("point").split(",");
        Point point = new Point(Integer.valueOf(pArr[0]), Integer.valueOf(pArr[1]));

        // Looking for the client-side being type
        String type = map.get("type");

        Being being = KvadratikGame.BEING_FACTORY.create(type, level, point, id);
        String name = map.getOrDefault("name", null);
        being.setName(name);
    }

    public void inBeingType(Packet packet) {
        if (packet.getType() != PacketType.SERVER_BEING_TYPE) {
            Logger.error("Unable to read a being type from " + packet.getType().name());
            return;
        }        

        Map<String, String> map = packet.toMap();

        // Getting a server-side unique ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the same entity
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            String type = map.get("type");
            being.morph(type);
        }
    }

    public void inLevel(Packet packet) {
        if (packet.getType() != PacketType.SERVER_LEVEL) {
            Logger.error("Unable to build a level from " + packet.getType().name());
            return;
        }
        Level level = JSON.fromJsonLevel(game, packet.toString(), true);
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

    public void inPoint(Packet packet) {
        Map<String, String> map = packet.toMap();
        int id = Integer.valueOf(map.get("id"));
        int x = Integer.valueOf(map.get("x"));
        int y = Integer.valueOf(map.get("y"));

        // Looking for the desired entity
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            being.teleport(x, y);
        }
    }

}
