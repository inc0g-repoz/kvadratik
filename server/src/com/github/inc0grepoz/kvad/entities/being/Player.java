package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.Vector;

public class Player extends Being {

    private final Connection connection;
    private final String name;
    private final Color chatColor = RGB.random();
    private final Queue<Packet> queuedPackets = new LinkedList<>();

    public Player(Connection connection, String name, Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, String type) {
        super(level, rect, collSize, collOffset, type);
        this.connection = connection;
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public Color getChatColor() {
        return chatColor;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (IOException e) {
        }
    }

    public void queue(Packet packet) {
        queuedPackets.add(packet);
    }

    public void flushQueuedPackets() throws IOException {
        OutputStream out = connection.getOutputStream();

        synchronized (queuedPackets) {
            for (Packet packet : queuedPackets) {
                packet.send(out);
            }
            queuedPackets.clear();
        }
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        super.packetEntries(map);
        map.put("name", name);
    }

}
