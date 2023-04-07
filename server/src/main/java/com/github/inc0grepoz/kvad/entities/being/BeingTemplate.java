package com.github.inc0grepoz.kvad.entities.being;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;

import lombok.Getter;

public class BeingTemplate {

    private final @Getter String type;
    private final Dimension size, collSize;
    private final Vector collOffset;

    public BeingTemplate(String type, Dimension size,
            Dimension collSize, Vector collOffset) {
        this.type = type;
        this.size = size;
        this.collSize = collSize;
        this.collOffset = collOffset;
    }

    public BeingTemplate(String type, Dimension size) {
        this(type, size, null, null);
    }

    public Player createPlayer(Connection connection, String name,
            Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        Player player = new Player(connection, name, level, rect, collSize, collOffset, type);
        level.getServer().players.add(player);
        level.getBeings().add(player);
        return player;
    }

    public Being create(Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        Being being = new Being(level, rect, collSize, collOffset, type);
        level.getBeings().add(being);
        return being;
    }

}
