package com.github.inc0grepoz.kvad.protocol;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Downloader;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.Unzipper;

public class PacketUtil {

    private final KvadratikGame game;

    public PacketUtil(KvadratikGame game) {
        this.game = game;
    }

    public void outAnim() {
        if (game.getClient().isConnected()) {
            String name = game.getLevel().getPlayer().getAnim().name();
            Packet.out(PacketType.CLIENT_PLAYER_ANIM, name).queue(game.getClient());
        }
    }

    public void outPoint(Being being, double moveX, double moveY) {
        if (!game.getClient().isConnected()
                || being.getId() != game.getLevel().getPlayer().getId()) {
            return;
        }

        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append("x=");
        sb.append(rect.x);
        sb.append(";y=");
        sb.append(rect.y);
        Packet.out(PacketType.CLIENT_PLAYER_POINT, sb.toString()).queue(game.getClient());
    }

    public void inAnim(Packet packet) {
        Map<String, String> map = packet.toMap();

        // Getting a server-side ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the client-side anim
        Anim anim = Anim.valueOf(map.get("anim"));

        // Looking for the desired being
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            being.applyAnim(anim);
        }
    }

    public void inAssets(Packet packet) {
        try {
            URL url = new URL(packet.string);

            Logger.info("Measuring the assets zip-file size");
            int size = Downloader.getFileSize(url);

            String fileName = UUID.nameUUIDFromBytes((url + ":" + size).getBytes())
                    .toString().replace("-", "");

            File assetsDir = new File("server-assets/" + fileName);
            if (assetsDir.exists() && assetsDir.isDirectory()) {
                Logger.info("Found an up-to-date version of the assets-pack");
            } else {
                File assetsZip = new File(assetsDir, fileName.concat(".zip"));
                Logger.info("Downloading the assets from " + packet.string);
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
        int id = Integer.valueOf(packet.string);
        game.getLevel().getBeings().removeIf(being -> {
            return being.getId() == id;
        });
    }

    public void inBeingSpawn(Packet packet) {
        Map<String, String> map = packet.toMap();

        // Getting a server-side ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the same being
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
        Point point = new Point(Double.valueOf(pArr[0]), Double.valueOf(pArr[1]));

        // Looking for the client-side being type
        String type = map.get("type");

        Being being = KvadratikGame.BEING_FACTORY.create(type, level, point, id);
        String name = map.getOrDefault("name", null);
        being.setName(name);
    }

    public void inBeingTeleport(Packet packet) {
        Map<String, String> map = packet.toMap();

        // Getting a server-side ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the same being
        Level level = game.getLevel();
        Being being = level.getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being == null) {
            Logger.error("Tried to teleport a being with an invalid ID");
            return;
        }

        // Teleporting that being
        String[] pArr = map.get("point").split(",");
        being.teleport(Double.valueOf(pArr[0]), Double.valueOf(pArr[1]));
    }

    public void inBeingType(Packet packet) {
        Map<String, String> map = packet.toMap();

        // Getting a server-side ID
        String idStr = map.getOrDefault("id", null);
        int id = idStr == null ? -1 : Integer.valueOf(idStr);

        // Looking for the same being
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            String type = map.get("type");
            being.morph(type);
        }
    }

    public void inLevel(Packet packet) {
        Level level = JSON.fromJsonLevel(packet.string, true);
        game.setLevel(level);
    }

    public void inPlayerMessage(Packet packet) {
        KvadratikClient client = game.getClient();
        if (!client.isConnected()) {
            return;
        }

        Message message = Message.deserialize(packet.string);
        client.getChat().print(message);
    }

    public void inPoint(Packet packet) {
        Map<String, String> map = packet.toMap();
        int id = Integer.valueOf(map.get("id"));
        double x = Double.valueOf(map.get("x"));
        double y = Double.valueOf(map.get("y"));

        // Looking for the desired being
        Being being = game.getLevel().getBeings().stream()
                .filter(b -> b.getId() == id).findFirst().orElse(null);
        if (being != null) {
            being.teleport(x, y);
        }
    }

    public void inTransferControl(Packet packet) {
        int id = Integer.valueOf(packet.string);
        Level level = game.getLevel();
        Being being = level.getBeings().stream()
                .filter(b -> id == b.getId())
                .findFirst().orElse(null);
        if (being != null) {
            level.setPlayer(being);
        }
    }

}
