package com.github.inc0grepoz.kvad.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class KvadratikClient {

    private final Logger logger;

    private int port;
    private String host;
    private Socket socket;

    public KvadratikClient(String host, int port) {
        this.logger = Logger.getLogger(getClass().getSimpleName());
        this.host = host;
        this.port = port;
    }

    public Logger getLogger() {
        return logger;
    }

    public void send(String message) {
        send(message.getBytes(StandardCharsets.UTF_8));
    }

    public void send(byte[] bytes) {
        connect();
        if (socket == null) {
            logger.warning("Unable to connect to the socket server");
        } else {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(bytes);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            socket = null;
        }
    }

    public void reconnect(String host, int port) {
        if (socket == null || !socket.isConnected()) {
            try {
                socket = new Socket(host, port);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void connect() {
        if (socket == null || !socket.isConnected()) {
            try {
                socket = new Socket(host, port);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
