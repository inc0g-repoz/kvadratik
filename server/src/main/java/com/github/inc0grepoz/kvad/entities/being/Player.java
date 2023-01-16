package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Map;

import com.github.inc0grepoz.kvad.chat.Message;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.Vector;

import lombok.Getter;

public class Player extends Being {

    private final @Getter Connection connection;
    private final @Getter String name;
    private final @Getter Color chatColor = RGB.random();

    public Player(Connection connection, String name, Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, String type) {
        super(level, rect, collSize, collOffset, type);
        this.connection = connection;
        this.name = name;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (IOException e) {
        }
    }

    public void sendMessage(Message message) {
        getLevel().getServer().packetUtil.outChat(this, message);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        super.packetEntries(map);
        map.put("name", name);
    }

}
