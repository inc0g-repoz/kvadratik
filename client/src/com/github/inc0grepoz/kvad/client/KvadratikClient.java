package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.chat.Chat;
import com.github.inc0grepoz.kvad.entities.chat.Message;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.Worker;

import lombok.Getter;
import lombok.Setter;

public class KvadratikClient extends Worker {

    private final KvadratikGame game;
    private final Queue<Packet> queue = new LinkedList<>();
    private final @Getter PacketUtil packetUtil;
    private final @Getter Chat chat = new Chat(this);

    private @Getter @Setter int port;
    private @Setter String host, nickname;
    private Socket socket;

    public KvadratikClient(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
        packetUtil = new PacketUtil(game);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public boolean isInfoProvided() {
        return nickname != null && host != null && port != 0;
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
        String text = "Connecting to " + host + ":" + port;
        Logger.info(text);

        Message message = new Message();
        message.addComponent(text, Color.ORANGE);
        chat.print(message);

        socket = new Socket(host, port);
        PacketType.CLIENT_LOGIN.create(nickname).queue(this);
    }

    @Override
    protected void work() {
        if (!isConnected()) {
            return;
        }

        // Reading the ingoing packets
        for (Packet packet : Packet.in(socket)) {
            switch (packet.getType()) {
                case SERVER_ASSETS_URL: {
                    packetUtil.inAssets(packet);
                    break;
                }
                case SERVER_BEING_ANIM: {
                    packetUtil.inAnim(packet);
                    break;
                }
                case SERVER_BEING_DESPAWN: {
                    packetUtil.inBeingDespawn(packet);
                    break;
                }
                case SERVER_BEING_POINT: {
                    packetUtil.inPoint(packet);
                    break;
                }
                case SERVER_BEING_SPAWN: {
                    packetUtil.inBeingSpawn(packet);
                    break;
                }
                case SERVER_BEING_TYPE: {
                    packetUtil.inBeingType(packet);
                    break;
                }
                case SERVER_CHAT_MESSAGE: {
                    packetUtil.inPlayerMessage(packet);
                    break;
                }
                case SERVER_LEVEL: {
                    packetUtil.inLevel(packet);
                    break;
                }
                case SERVER_TRANSFER_CONTROL: {
                    int id = Integer.valueOf(packet.toString());
                    Level level = game.getLevel();
                    Being being = level.getBeings().stream()
                            .filter(b -> id == b.getId())
                            .findFirst().orElse(null);
                    if (being != null) {
                        level.setPlayer(being);
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
                disconnect();
                Logger.error("Connection reset");

                Message message = new Message();
                message.addComponent("Connection reset!", Color.RED);
                chat.print(message);
            }
        }
    }

}
