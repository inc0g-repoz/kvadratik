package com.github.inc0grepoz.kvad.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.chat.Chat;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.Worker;

public class KvadratikClient extends Worker {

    private final KvadratikGame game;
    private final PacketUtil packetUtil;
    private final Queue<Packet> queue = new LinkedList<>();
    private final Chat chat;

    private int port;
    private String host, nickname;
    private Socket socket;

    public KvadratikClient(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
        packetUtil = new PacketUtil(game);
        chat = new Chat(this);
    }

    public PacketUtil getPacketUtil() {
        return packetUtil;
    }

    public Chat getChat() {
        return chat;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
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
        if (isConnected()) {
            queue.add(packet);
        }
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

    @Override
    protected void work() {
        if (!isConnected()) {
            return;
        }

        // Reading the ingoing packets
        for (Packet packet : Packet.in(socket)) {
            switch (packet.getType()) {
                case SERVER_BEING_ANIM: {
                    
                }
                case SERVER_BEING_DESPAWN: {
                    int id = Integer.valueOf(packet.toString());
                    game.getLevel().getBeings().removeIf(being -> {
                        return being.getId() == id;
                    });
                    break;
                }
                case SERVER_BEING_SPAWN: {
                    packetUtil.createBeing(packet);
                    break;
                }
                case SERVER_CHAT_MESSAGE: {
                    packetUtil.readPlayerMessage(packet);
                    break;
                }
                case SERVER_LEVEL: {
                    packetUtil.buildLevel(packet);
                    break;
                }
                case SERVER_TRANSFER_CONTROL: {
                    int id = Integer.valueOf(packet.toString());
                    Level level = game.getLevel();
                    Being being = level.getBeings().stream()
                            .filter(b -> id == b.getId())
                            .findFirst().orElse(null);
                    if (being != null) {
                        level.setPlayerBeing(being);
                    }
                }
                default:
            }
        }

        // Flushing the outgoing packets
        if (queue.size() != 0) {
            synchronized (queue) {
                for (Packet packet = null; (packet = queue.poll()) != null;) {
                    try {
                        packet.send(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                queue.clear();
            }
        }

        // Prevent the server from resetting the connection
        else {
            try {
                PacketType.CLIENT_KEEP_ALIVE.create(" ").send(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
