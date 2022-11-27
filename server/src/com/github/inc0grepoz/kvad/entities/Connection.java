package com.github.inc0grepoz.kvad.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class Connection {

    public boolean timeoutImmune;

    private final KvadratikServer kvad;
    private final Socket socket;

    private long lastResponse = System.currentTimeMillis();

    public Connection(KvadratikServer kvad, Socket socket) {
        this.kvad = kvad;
        this.socket = socket;
    }

    public boolean equals(Connection c) {
        return c instanceof Connection && c.getInetAddress().getHostAddress()
                .equals(socket.getInetAddress().getHostAddress());
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public boolean hasExpired() {
        return !timeoutImmune && System.currentTimeMillis() - lastResponse > kvad.settings.timeout;
    }

    public void tick() {
        lastResponse = System.currentTimeMillis();
    }

    public void close() throws IOException {
        socket.close();
    }

}
