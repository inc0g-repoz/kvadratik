package com.github.inc0grepoz.kvad.protocol;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.github.inc0grepoz.kvad.client.KvadratikClient;

public class Packet {

    public static Packet[] in(Socket sock) {
        List<Packet> packets = new ArrayList<Packet>();

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

        return packets.stream().toArray(Packet[]::new);
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

    public void send(KvadratikClient client) {
        try {
            OutputStream out = client.getSocket().getOutputStream();
            out.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
