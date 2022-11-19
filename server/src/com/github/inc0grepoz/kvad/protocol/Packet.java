package com.github.inc0grepoz.kvad.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.Player;
import com.github.inc0grepoz.kvad.utils.Logger;

public class Packet {

    public static boolean logging = true;

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

            if (logging && packet.type != PacketType.CLIENT_KEEP_ALIVE) {
                StringBuilder sbPack = new StringBuilder();
                sbPack.append("Packet In (");
                sbPack.append(packet.id);
                sbPack.append("):\nBase64: ");
                sbPack.append(packet.b64);
                sbPack.append("\nString: ");
                sbPack.append(packet.string);
                Logger.info(sbPack.toString());
            }
        }

        return packets;
    }

    public static Packet out(PacketType type, String string) {
        Packet packet = new Packet();
        packet.type = type;
        packet.string = string;
        packet.id = type.getId();

        byte[] bytes = string.getBytes();
        packet.b64 = Base64.getEncoder().encodeToString(bytes);
        packet.data = (packet.id + " " + packet.b64 + " ").getBytes();
        return packet;
    }

    private int id;
    private byte[] data;
    private PacketType type;
    private String string, b64;

    @Override
    public String toString() {
        return string;
    }

    public PacketType getType() {
        return type;
    }

    public void send(OutputStream out) throws IOException {
        if (logging) {
            StringBuilder sbPack = new StringBuilder();
            sbPack.append("Packet Out (");
            sbPack.append(id);
            sbPack.append("):\nBase64: ");
            sbPack.append(b64);
            sbPack.append("\nString: ");
            sbPack.append(string);
            Logger.info(sbPack.toString());
        }

        out.write(data, 0, data.length);
    }

    public void queue(Player player) {
        player.queue(this);
    }

}
