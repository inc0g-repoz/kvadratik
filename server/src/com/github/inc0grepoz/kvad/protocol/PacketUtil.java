package com.github.inc0grepoz.kvad.protocol;

import com.github.inc0grepoz.kvad.entities.Being;
import com.github.inc0grepoz.kvad.entities.Player;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class PacketUtil {

    private final KvadratikServer kvad;

    public PacketUtil(KvadratikServer kvad) {
        this.kvad = kvad;
    }

    public void anim(Being being) {
        String content = being.getId() + "," + being.getAnim().name();
        Packet packet = PacketType.SERVER_BEING_ANIM.create(content);
        allExcludePlayer(packet, being);
    }

    public void speed(Being being) {
        String content = being.getId() + "," + being.getAnim().name();
        Packet packet = PacketType.SERVER_BEING_SPEED.create(content);
        allExcludePlayer(packet, being);
    }

    public void sendLevel(Player player) {
        PacketType.SERVER_LEVEL.create(kvad.getLevel().toString()).queue(player);
    }

    public void spawnBeing(Player player, Being being) {
        PacketType.SERVER_BEING_SPAWN.create(being.toString()).queue(player);
    }

    public void spawnBeingForAll(Being being) {
        Packet packet = PacketType.SERVER_BEING_SPAWN.create(being.toString());
        allExcludePlayer(packet, being);
    }

    public void despawnBeing(Being being) {
        String id = String.valueOf(being);
        Packet packet = PacketType.SERVER_BEING_DESPAWN.create(id);
        kvad.getPlayers().forEach(packet::queue);
    }

    public void transferControl(Player player, Being being) {
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
