package com.github.inc0grepoz.kvad.protocol;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class PacketUtil {

    private final KvadratikServer kvad;

    public PacketUtil(KvadratikServer kvad) {
        this.kvad = kvad;
    }

    public void inPlayerRect(Packet packet, Player player) {
        Map<String, String> map = packet.toMap();
        int x = Integer.valueOf(map.get("x"));
        int y = Integer.valueOf(map.get("y"));
        int w = Integer.valueOf(map.get("w"));
        int h = Integer.valueOf(map.get("h"));
        Rectangle rect = player.getRectangle();
        rect.x = x;
        rect.y = y;
        rect.width = w;
        rect.height = h;
        outBeingRect(player);
    }

    public void outBeingRect(Being being) {
        Rectangle rect = being.getRectangle();
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(being.getId());
        sb.append(";x=");
        sb.append(rect.x);
        sb.append(";y=");
        sb.append(rect.y);
        sb.append(";w=");
        sb.append(rect.width);
        sb.append(";h=");
        sb.append(rect.height);
        Packet packet = PacketType.SERVER_BEING_RECT.create(sb.toString());
        kvad.getPlayers().forEach(packet::queue);
    }

    public void outChat(Player player, String message) {
        Color color = player.getChatColor();
        StringBuilder sb = new StringBuilder();
        sb.append("color=");
        sb.append(color.getRed());
        sb.append(",");
        sb.append(color.getGreen());
        sb.append(",");
        sb.append(color.getBlue());
        sb.append(";name=");
        sb.append(player.getName());
        sb.append(";text=");
        sb.append(message);
        Packet packet = PacketType.SERVER_CHAT_MESSAGE.create(sb.toString());
        kvad.getPlayers().forEach(packet::queue);
    }

    public void outAnim(Being being) {
        String content = "id=" + being.getId() + ";anim=" + being.getAnim().name();
        Packet packet = PacketType.SERVER_BEING_ANIM.create(content);
        allExcludePlayer(packet, being);
    }

    public void outLevel(Player player) {
        PacketType.SERVER_LEVEL.create(kvad.getLevel().toString()).queue(player);
    }

    public void outSpawnBeing(Player player, Being being) {
        PacketType.SERVER_BEING_SPAWN.create(being.toString()).queue(player);
    }

    public void outSpawnBeingForAll(Being being) {
        Packet packet = PacketType.SERVER_BEING_SPAWN.create(being.toString());
        allExcludePlayer(packet, being);
    }

    public void outDespawnBeing(Being being) {
        String id = String.valueOf(being.getId());
        Packet packet = PacketType.SERVER_BEING_DESPAWN.create(id);
        kvad.getPlayers().forEach(packet::queue);
    }

    public void outTransferControl(Player player, Being being) {
        PacketType.SERVER_TRANSFER_CONTROL.create(String.valueOf(being.getId())).queue(player);
    }

    private void allExcludePlayer(Packet packet, Being being) {
        if (being instanceof Player) {
            kvad.getPlayers().forEach(player -> {
                if (player.getId() != being.getId()) {
                    packet.queue(player);
                }
            });
        } else {
            kvad.getPlayers().forEach(packet::queue);
        }
    }

}
