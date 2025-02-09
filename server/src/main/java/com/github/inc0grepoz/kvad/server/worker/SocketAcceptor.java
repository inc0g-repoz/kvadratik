package com.github.inc0grepoz.kvad.server.worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.github.inc0grepoz.kvad.common.worker.Worker;
import com.github.inc0grepoz.kvad.server.entities.Connection;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

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
                kvad.connections.add(new Connection(kvad, newSocket));
            }
        } catch (IOException e) {
        }
    }

}
