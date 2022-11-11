package com.github.inc0grepoz.kvad.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.utils.XML;
import com.github.inc0grepoz.kvad.worker.Worker;

public class KvadratikClient extends Worker {

    private final KvadratikGame game;
    private final Queue<Packet> queue = new LinkedList<>();

    private int port;
    private String host, nickname;
    private Socket socket;

    public KvadratikClient(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
    }

    @Override
    protected void work() {
        if (!isConnected()) {
            return;
        }

        // Reading the ingoing packets
        for (Packet packet : Packet.in(socket)) {
            switch (packet.type()) {
                case SERVER_BEING_SPAWN: {
                    packet.createBeing(game.getLevel());
                    break;
                }
                case SERVER_LEVEL: {
                    Level level = new Level(game, XML.fromString(packet.toString()));
                    game.setLevel(level);
                    break;
                }
                default:
            }
        }

        // Flushing the outgoing packets
        synchronized (queue) {
            for (Packet packet : queue) {
                try {
                    packet.send(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            queue.clear();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket == null || !socket.isConnected();
    }

    public boolean isInfoProvided() {
        return nickname != null && host != null && port != 0;
    }

    public String getNickname() {
        return nickname;
    }

    public String getServerIp() {
        return host;
    }

    public int getServerPort() {
        return port;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setServerIp(String host) {
        this.host = host;
    }

    public void setServerPort(int port) {
        this.port = port;
    }

    public void queue(Packet packet) {
        queue.add(packet);
    }

    public void disconnect() {
        if (socket != null) {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            socket = null;
        }
    }

    public void connect() throws UnknownHostException, IOException {
        Logger.info("Connecting to " + host + ":" + port);
        socket = new Socket(host, port);
        Packet.out(PacketType.CLIENT_LOGIN, nickname);
    }

}
