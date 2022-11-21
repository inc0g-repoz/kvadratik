package com.github.inc0grepoz.kvad.entities;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingType;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;

public class Player extends Being {

    private final Connection connection;
    private final String name;
    private final Queue<Packet> queuedPackets = new LinkedList<>();

    public Player(Connection connection, String name, int[] rect,
            Level level) {
        super(level, rect, BeingType.IOMOR);
        this.connection = connection;
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
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
