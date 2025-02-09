package com.github.inc0grepoz.kvad.server.entities;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.common.utils.RGB;
import com.github.inc0grepoz.kvad.server.protocol.Packet;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

public class Connection {

    public boolean timeoutImmune;
    public Color chatColor = RGB.random();

    private final KvadratikServer kvad;
    private final Socket socket;
    private final Queue<Packet> queue = new LinkedList<>();

    private long lastResponse = System.currentTimeMillis();

    public Connection(KvadratikServer kvad, Socket socket) {
        this.kvad = kvad;
        this.socket = socket;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Connection
                && ((Connection) obj).getInetAddress().getHostAddress()
                        .equals(socket.getInetAddress().getHostAddress());
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void queue(Packet packet) {
        queue.add(packet);
    }

    public void flushQueuedPackets() throws IOException {
        OutputStream out = socket.getOutputStream();
        for (Packet packet; (packet = queue.poll()) != null;) {
            packet.send(out);
        }
    }

    public boolean hasExpired() {
        return socket.isClosed() || !timeoutImmune && System.currentTimeMillis() - lastResponse > kvad.settings.timeout;
    }

    public void tick() {
        lastResponse = System.currentTimeMillis();
    }

    public void close() throws IOException {
        socket.close();
    }

}
