package com.github.inc0grepoz.kvad.protocol;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.Logger;

public class PacketUtil {

    private final KvadratikServer kvad;

    public PacketUtil(KvadratikServer kvad) {
        this.kvad = kvad;
    }

    public void inChat(Player player, Packet in) {
        String message = in.string;
        if (message.startsWith("!")) {
            kvad.handlePlayerCommand(player, message);
            return;
        }

        Color color = player.getChatColor();
        Message msg = new Message()
                .addComponent(player.getName() + ": ", color)
                .addComponent(message);

        Packet out = PacketType.SERVER_CHAT_MESSAGE.create(msg.toString());
        kvad.players.forEach(out::queue);
    }

    public void inLogin(Packet in, Connection connection) {
        /* The snippet bellow needs to be placed here later
         * when the packet related stuff fixing is done */

//      boolean already = players.stream().anyMatch(player -> {
//          return connection.equals(player.getConnection());
//      });
//      if (!already) {
//          // Add a new player to the list
//      }

        Level level = kvad.levels.get(0);
        Player player = level.getPlayerPreset().spawn(connection, in.string, level);

        // Sending the level data and all beings
        outAssets(player, kvad.settings.assetsLink);
        outLevel(player, level);
        outBeingSpawnForAll(player);
        level.getBeings().forEach(being -> outBeingSpawn(player, being));

        outTransferControl(player, player);

        String ip = connection.getInetAddress().getHostAddress();
        Logger.info(in.string + " joined the server from " + ip);
    }

    public void inPlayerAnim(Player player, Packet in) {
        Anim anim = Anim.valueOf(in.string);
        player.applyAnim(anim);
        outAnim(player);
    }

    public void inPlayerPoint(Player player, Packet in) {
        Map<String, String> map = in.toMap();
        int x = Integer.valueOf(map.get("x"));
        int y = Integer.valueOf(map.get("y"));
        player.teleport(x, y);
        outBeingPoint(player);
    }

    public void outAnim(Being being) {
        String content = "id=" + being.getId() + ";anim=" + being.getAnim().name();
        Packet packet = PacketType.SERVER_BEING_ANIM.create(content);
        allExcludePlayer(packet, being);
    }

    public void outAssets(Player player, String url) {
        PacketType.SERVER_ASSETS_URL.create(url).queue(player);
    }

    public void outBeingPoint(Being being) {
        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(being.getId());
        sb.append(";x=");
        sb.append(rect.x);
        sb.append(";y=");
        sb.append(rect.y);
        Packet packet = PacketType.SERVER_BEING_POINT.create(sb.toString());
        allExcludePlayer(packet, being);
    }

    public void outBeingType(Being being) {
        String content = "id=" + being.getId() + ";type=" + being.getType();
        Packet packet = PacketType.SERVER_BEING_TYPE.create(content);
        kvad.players.forEach(packet::queue);
    }

    public void outBeingSpawn(Player player, Being being) {
        PacketType.SERVER_BEING_SPAWN.create(being.toString()).queue(player);
    }

    public void outBeingSpawnForAll(Being being) {
        Packet packet = PacketType.SERVER_BEING_SPAWN.create(being.toString());
        allExcludePlayer(packet, being);
    }

    public void outBeingDespawn(Being being) {
        String id = String.valueOf(being.getId());
        Packet packet = PacketType.SERVER_BEING_DESPAWN.create(id);
        kvad.players.forEach(packet::queue);
    }

    public void outLevel(Player player, Level level) {
        PacketType.SERVER_LEVEL.create(level.toString()).queue(player);
    }

    public void outTransferControl(Player player, Being being) {
        PacketType.SERVER_TRANSFER_CONTROL.create(String.valueOf(being.getId())).queue(player);
    }

    private void allExcludePlayer(Packet packet, Being being) {
        if (being instanceof Player) {
            kvad.players.forEach(player -> {
                if (player.getId() != being.getId()) {
                    packet.queue(player);
                }
            });
        } else {
            kvad.players.forEach(packet::queue);
        }
    }

}
