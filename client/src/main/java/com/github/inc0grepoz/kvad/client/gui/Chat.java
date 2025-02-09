package com.github.inc0grepoz.kvad.client.gui;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.client.protocol.Packet;
import com.github.inc0grepoz.kvad.common.gui.Message;
import com.github.inc0grepoz.kvad.common.protocol.PacketType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Chat {

    public boolean locked, typing;
    public String draft;

    private final KvadratikClient kvad;
    private final Queue<Message> messages = new LinkedList<>();

    public void clear() {
        messages.clear();
    }

    public void send(String message) {
        Packet.out(PacketType.CLIENT_CHAT_MESSAGE, message).queue(kvad);
    }

    public void print(Message message) {
        if (messages.size() >= 8) {
            messages.remove();
        }
        messages.add(message);
    }

    public void render(Graphics gfx) {
        if (messages.isEmpty()) {
            return;
        }
        int y = 20;
        for (Message message : messages) {
            message.render(gfx, 10, y);
            y += 20;
        }
        if (typing) {
            gfx.drawString("> " + draft, 10, y);
        }
    }

}
