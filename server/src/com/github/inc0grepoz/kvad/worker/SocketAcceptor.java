package com.github.inc0grepoz.kvad.worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class SocketAcceptor extends Worker {

    private final KvadratikServer kvad;
    private final ServerSocket server;

    public SocketAcceptor(KvadratikServer kvad, int port) {
        super(50L);
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.kvad = kvad;
    }

    @Override
    protected void work() {
        try {
            Socket newSocket = server.accept();

            // Any new sockets?
            if (newSocket != null) {
                kvad.getConnections().add(new Connection(kvad, newSocket));
            }
        } catch (IOException e) {
        }
    }

}
