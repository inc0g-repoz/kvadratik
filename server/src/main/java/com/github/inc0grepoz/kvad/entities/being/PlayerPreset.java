package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Point;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class PlayerPreset {

    private final Level level;
    private final Point point;
    private final String type;

    public PlayerPreset(Level level, Point point, String type) {
        this.level = level;
        this.type = type;
        this.point = point;
    }

    public Player spawn(Connection connection, String name) {
        return KvadratikServer.BEING_FACTORY.createPlayer(connection, name, level, point, type);
    }

}
