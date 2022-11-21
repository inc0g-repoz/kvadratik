package com.github.inc0grepoz.kvad.worker;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Queue;

import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.protocol.Packet;
import com.github.inc0grepoz.kvad.protocol.PacketType;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.Logger;

public class PacketHandler extends Worker {

    private final KvadratikServer kvad;

    public PacketHandler(KvadratikServer kvad) {
        super(50L);
        this.kvad = kvad;
    }

    @Override
    protected void work() {
        // Reading the ingoing player packets
        acceptPlayers();

        // Resetting all timed out connections
        kvad.closeExpiredConnections();

        // Reading the ingoing non-player packets
        acceptMisc();

        // Sending the outgoing queued player packets
        flushPlayerPackets();
    }

    private void acceptMisc() {
        kvad.getConnections().forEach(connection -> {
            Queue<Packet> queue;

            // Handling an i/o exception for the case if a connection gets reset
            try {
                queue = Packet.in(connection);
            } catch (IOException e) {
                Logger.error("Tried to resolve an input stream of an invalid socket");
                return;
            }

            for (Packet packet : queue) {
                switch (packet.getType()) {
                    case CLIENT_LOGIN:
                        kvad.logPlayerIn(packet.toString(), connection);
                        break;
                    default:
                }
            }
        });
    }

    private void acceptPlayers() {
        kvad.getPlayers().forEach(player -> {
            Queue<Packet> queue;

            // Handling an i/o exception for the case if a connection gets reset
            try {
                queue = Packet.in(player.getConnection());
            } catch (Throwable e) {
                Logger.error("Tried to resolve an input stream of a disconnected player");
                return;
            }

            if (queue.size() != 0) {
                player.getConnection().tick();

                // Iterating through the packets from the queue
                for (Packet packet : queue) {
                    switch (packet.getType()) {
                        case CLIENT_CHAT_MESSAGE: {
                            String chatMsg = player.getName() + ": " + packet.toString();
                            Packet cmp = Packet.out(PacketType.SERVER_CHAT_MESSAGE, chatMsg);
                            kvad.getPlayers().forEach(cmp::queue);
                            break;
                        }
                        case CLIENT_PLAYER_ANIM: {
                            Anim anim = Anim.valueOf(packet.toString());
                            player.applyAnim(anim);
                            kvad.getPacketUtil().anim(player);
                            break;
                        }
                        case CLIENT_PLAYER_SPEED: {
                            //kvad.getPacketUtil().speed(player);
                            break;
                        }
                        case CLIENT_PLAYER_RECT: {
                            String[] rectStr = packet.toString().split(",");
                            Rectangle rect = player.getRectangle();
                            rect.x = Integer.valueOf(rectStr[0]);
                            rect.y = Integer.valueOf(rectStr[1]);
                            rect.width = Integer.valueOf(rectStr[2]);
                            rect.height = Integer.valueOf(rectStr[3]);
                            break;
                        }
                        default:
                    }
                }
            }
        });
    }

    private void flushPlayerPackets() {
        kvad.getPlayers().forEach(player -> {
            try {
                player.flushQueuedPackets();
            } catch (IOException e) {
                e.initCause(new IOException("Unable to send the queued packets"));
                e.printStackTrace();
            }
        });
    }

}
