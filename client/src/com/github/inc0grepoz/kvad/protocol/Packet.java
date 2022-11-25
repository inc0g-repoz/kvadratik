package com.github.inc0grepoz.kvad.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.utils.Logger;

public class Packet {

    public static boolean logging = true;

    public static Queue<Packet> in(Socket sock) {
        Queue<Packet> packets = new LinkedList<>();

        try {
            InputStream in = sock.getInputStream();

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

                if (logging) {
                    StringBuilder sbPack = new StringBuilder();
                    sbPack.append("Packet In (");
                    sbPack.append(packet.type.name());
                    sbPack.append("):\nString: ");
                    sbPack.append(packet.string);
                    Logger.info(sbPack.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public void queue(KvadratikClient client) {
        client.queue(this);
    }

    public void send(OutputStream out) throws IOException {
        if (logging && type != PacketType.CLIENT_KEEP_ALIVE) {
            StringBuilder sbPack = new StringBuilder();
            sbPack.append("Packet Out (");
            sbPack.append(type.name());
            sbPack.append("):\nString: ");
            sbPack.append(string);
            Logger.info(sbPack.toString());
        }

        out.write(data, 0, data.length);
    }

    Map<String, String> toMap(int eltsCount) {
        Map<String, String> map = new HashMap<>();
        String[] content = string.contains(";") ? string.split(";", eltsCount)
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
