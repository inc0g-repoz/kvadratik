package com.github.inc0grepoz.kvad.entities.being;

import java.io.IOException;
import java.util.Map;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.Message;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;

public class Player extends Being {

    private final @Getter Connection connection;
    private final @Getter String name;

    public Player(Connection connection, String name, Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, String type) {
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
    public void delete() {
        super.delete();
        getLevel().getServer().players.removeIf(p -> p.getId() == getId());
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        super.packetEntries(map);
        map.put("name", name);
    }

}
