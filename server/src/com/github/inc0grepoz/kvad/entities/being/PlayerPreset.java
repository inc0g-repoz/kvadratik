package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Point;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class PlayerPreset {

    private final String type;
    private final Point point;

    public PlayerPreset(String type, Point point) {
        this.type = type;
        this.point = point;
    }

    public Player spawn(Connection connection, String name, Level level) {
        return KvadratikServer.BEING_FACTORY.createPlayer(connection, name, level, point, type);
    }

}
