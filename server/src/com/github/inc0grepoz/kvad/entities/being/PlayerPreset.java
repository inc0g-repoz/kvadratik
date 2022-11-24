package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Point;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class PlayerPreset {

    private final BeingTemplate temp;
    private final Point point;

    public PlayerPreset(BeingTemplate temp, Point point) {
        this.temp = temp;
        this.point = point;
    }

    public Player spawn(Connection connection, String name, Level level) {
        Player player = temp.createPlayer(connection, name, level, point);
        level.getGame().getPlayers().add(player);
        return player;
    }

}
