package com.github.inc0grepoz.kvad.protocol;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;
import java.util.stream.Stream;

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

    public void inLogin(Packet in, Connection cxn) {
        String playerName = in.string;
        Level level = kvad.levels.get(0);

        boolean connected = level.getServer().players.stream()
                .anyMatch(p -> p.getName().equals(playerName));
        if (connected) {
            return;
        }

        PacketType.SERVER_ASSETS_URL.create(kvad.settings.assetsLink).queue(cxn);
        level.join(cxn, level, playerName);

        String ip = cxn.getInetAddress().getHostAddress();
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
        allSameLevelExcludePlayer(packet, being);
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
        allSameLevelExcludePlayer(packet, being);
    }

    public void outBeingSpawn(Player player, Being being) {
        PacketType.SERVER_BEING_SPAWN.create(being.toString()).queue(player);
    }

    public void outBeingSpawnForAll(Being being) {
        Packet packet = PacketType.SERVER_BEING_SPAWN.create(being.toString());
        allSameLevelExcludePlayer(packet, being);
    }

    public void outBeingDespawn(Being being) {
        String id = String.valueOf(being.getId());
        Packet packet = PacketType.SERVER_BEING_DESPAWN.create(id);
        allSameLevel(packet, being.getLevel());
    }

    public void outBeingType(Being being) {
        String content = "id=" + being.getId() + ";type=" + being.getType();
        Packet packet = PacketType.SERVER_BEING_TYPE.create(content);
        allSameLevel(packet, being.getLevel());
    }

    public void outChat(Player player, Message message) {
        Packet out = PacketType.SERVER_CHAT_MESSAGE.create(message.toString());
        out.queue(player);
    }

    public void outLevel(Player player, Level level) {
        PacketType.SERVER_LEVEL.create(level.toString()).queue(player);
    }

    public void outTransferControl(Player player, Being being) {
        PacketType.SERVER_TRANSFER_CONTROL.create(String.valueOf(being.getId())).queue(player);
    }

    private void allSameLevelExcludePlayer(Packet packet, Being being) {
        String bngLvlName = being.getLevel().getName();
        Stream<Player> players = kvad.players.stream()
                .filter(p -> p.getLevel().getName().equals(bngLvlName));
        if (being instanceof Player) {
            players.forEach(player -> {
                if (player.getId() != being.getId()) {
                    packet.queue(player);
                }
            });
        } else {
            players.forEach(packet::queue);
        }
    }

    private void allSameLevel(Packet packet, Level level) {
        String bngLvlName = level.getName();
        kvad.players.forEach(player -> {
            if (player.getLevel().getName().equals(bngLvlName)) {
                packet.queue(player);
            }
        });
    }

}
