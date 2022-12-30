package com.github.inc0grepoz.kvad.chat;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.kvad.client.KvadratikClient;
import com.github.inc0grepoz.kvad.protocol.PacketType;

public class Chat {

    public boolean locked, typing;
    public String draft;

    private final KvadratikClient kvad;
    private final List<Message> messages = new LinkedList<>();

    public Chat(KvadratikClient kvad) {
        this.kvad = kvad;
    }

    public void send(String message) {
        PacketType.CLIENT_CHAT_MESSAGE.create(message).queue(kvad);
    }

    public void print(Message message) {
        if (messages.size() >= 8) {
            messages.remove(0);
        }
        messages.add(message);
    }

    public void render(Graphics gfx) {
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
