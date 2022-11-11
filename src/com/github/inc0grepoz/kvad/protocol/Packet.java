package com.github.inc0grepoz.kvad.protocol;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.IntStream.Builder;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingType;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;

public class Packet {

    public static Queue<Packet> in(Socket sock) {
        Queue<Packet> packets = new LinkedList<>();

        try {
            InputStream in = sock.getInputStream();
            DataInputStream dIn = new DataInputStream(in);
            String msg = new BufferedReader(new InputStreamReader(dIn))
                    .lines().collect(Collectors.joining());

            String[] split = msg.split(" ");
            for (int i = 0; i + 1 < split.length; i += 2) {
                Packet packet = new Packet();
                packet.id = Integer.valueOf(split[i].replaceAll("[^0-9]+", ""));
                packet.b64 = split[i + 1];
                packet.string = new String(Base64.getDecoder().decode(packet.b64.getBytes()));
                packet.type = PacketType.byId(packet.id);
                packet.data = (packet.id + " " + packet.b64 + " ").getBytes();
                packets.add(packet);
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

    public PacketType type() {
        return type;
    }

    public void queue(KvadratikClient client) {
        client.queue(this);
    }

    public void send(OutputStream out) throws IOException {
        out.write(data, 0, data.length);
    }

    public void createBeing(Level level) {
        if (type != PacketType.SERVER_BEING_SPAWN) {
            Logger.error("Unable to init a being from " + type.name());
        }

        Map<String, String> map = toMap();

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

        // Looking for the client-side being type
        BeingType type = BeingType.valueOf(map.get("type"));

        Being being = new Being(b.build().toArray(), level, type);
        level.getBeings().add(being);
    }

    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        String[] content = string.contains(";") ? string.split(";") : new String[] { string };

        for (int i = 0; i < content.length; i++) {
            if (content[i].contains("=")) {
                String[] kvp = content[i].split("=");
                map.put(kvp[0], kvp[1]);
            }
        }

        return map;
    }

}
