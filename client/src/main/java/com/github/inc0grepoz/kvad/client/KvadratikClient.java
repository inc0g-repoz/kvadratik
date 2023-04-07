package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.chat.Chat;
import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.utils.Logger;
import com.github.inc0grepoz.kvad.worker.Worker;

import lombok.Getter;
import lombok.Setter;

public class KvadratikClient extends Worker {

    private final Queue<Packet> queue = new LinkedList<>();
    private final @Getter PacketUtil packetUtil;
    private final @Getter Chat chat = new Chat(this);

    private @Setter int port;
    private @Setter String host, nickname;
    private Socket socket;

    public KvadratikClient(KvadratikGame game, long delay) {
        super(delay);
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
        Packet.out(PacketType.CLIENT_LOGIN, nickname).queue(this);
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
                case SERVER_BEING_TELEPORT: {
                    packetUtil.inBeingTeleport(packet);
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
                    packetUtil.inTransferControl(packet);
                    break;
                }
                default:
            }
        }

        // Flushing the outgoing packets
        if (queue.size() != 0) {
            for (Packet packet; (packet = queue.poll()) != null;) {
                try {
                    packet.send(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Prevent the server from resetting the connection
        else {
            try {
                Packet.out(PacketType.CLIENT_KEEP_ALIVE, " ").send(socket.getOutputStream());
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
