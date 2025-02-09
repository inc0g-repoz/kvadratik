package com.github.inc0grepoz.kvad.server.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.github.inc0grepoz.kvad.common.protocol.PacketType;
import com.github.inc0grepoz.kvad.common.utils.Logger;
import com.github.inc0grepoz.kvad.server.entities.Connection;
import com.github.inc0grepoz.kvad.server.entities.being.Player;

public class Packet {

    public static boolean logging = false;
    public static PacketType[] filter = {

            PacketType.CLIENT_KEEP_ALIVE,
            PacketType.CLIENT_PLAYER_ANIM,
            PacketType.SERVER_BEING_ANIM

    };

    public static Queue<Packet> in(Connection connection) throws IOException {
        Queue<Packet> packets = new LinkedList<>();
        InputStream in = connection.getInputStream();

        // Got nothing to read
        if (in.available() == 0) {
            return packets;
        }

        // Converting into a string
        StringBuilder sbMsg = new StringBuilder();
        for (int c = in.read(); in.available() != 0; c = in.read()) {
            sbMsg.append((char) c);
        }
        String msg = sbMsg.toString();

        // Initializing some packet instances
        String[] split = msg.split(" ");
        for (int i = 0; i + 1 < split.length; i += 2) {
            Packet packet = new Packet();
            packet.id = Integer.valueOf(split[i].replaceAll("[^0-9]+", ""));
            packet.b64 = split[i + 1];
            packet.string = new String(Base64.getDecoder().decode(packet.b64.getBytes()));
            packet.type = PacketType.byId(packet.id);
            packet.data = (packet.id + " " + packet.b64 + " ").getBytes();
            packets.add(packet);

            if (logging && !isExcluded(packet.type)) {
                StringBuilder sbPack = new StringBuilder();
                sbPack.append("Packet In (");
                sbPack.append(packet.type.name());
                sbPack.append("):\nString: ");
                sbPack.append(packet.string);
                Logger.info(sbPack.toString());
            }
        }

        return packets;
    }

    static Packet out(PacketType type, String string) {
        Packet packet = new Packet();
        packet.type = type;
        packet.string = string;
        packet.id = type.getId();

        byte[] bytes = string.getBytes();
        packet.b64 = Base64.getEncoder().encodeToString(bytes);
        packet.data = (packet.id + " " + packet.b64 + " ").getBytes();
        return packet;
    }

    private static boolean isExcluded(PacketType pt) {
        for (int i = 0; i < filter.length; i++) {
            if (filter[i] == pt) {
                return true;
            }
        }
        return false;
    }

    String string, b64;

    private int id;
    private byte[] data;
    private PacketType type;

    public PacketType getType() {
        return type;
    }

    public void send(OutputStream out) throws IOException {
        if (logging && !isExcluded(type)) {
            StringBuilder sbPack = new StringBuilder();
            sbPack.append("Packet Out (");
            sbPack.append(type.name());
            sbPack.append("):\nString: ");
            sbPack.append(string);
            Logger.info(sbPack.toString());
        }

        out.write(data, 0, data.length);
    }

    public void queue(Connection cxn) {
        cxn.queue(this);
    }

    public void queue(Player player) {
        queue(player.getConnection());
    }

    Map<String, String> toMap(int eltsCount) {
        Map<String, String> map = new HashMap<>();
        String[] content = string.contains(";")
                ? string.split(";", eltsCount)
                : new String[] { string };
        for (int i = 0; i < content.length; i++) {
            if (content[i].contains("=")) {
                String[] kvp = content[i].split("=");
                map.put(kvp[0], kvp[1]);
            }
        }
        return map;
    }

    Map<String, String> toMap() {
        return toMap(0);
    }

}
