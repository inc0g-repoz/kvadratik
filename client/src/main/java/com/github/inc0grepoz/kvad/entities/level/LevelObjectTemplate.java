package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.utils.Vector;

import lombok.Getter;

public abstract class LevelObjectTemplate {

    private final @Getter String type;

    final Dimension size, collSize;
    final Vector collOffset;
    final boolean collide;

    public LevelObjectTemplate(String type, Dimension size,
            Dimension collSize, Vector collOffset, boolean collide) {
        this.type = type;
        this.size = size;
        this.collSize = collSize;
        this.collOffset = collOffset;
        this.collide = collide;
    }

    public LevelObjectTemplate(String type, Dimension size) {
        this(type, size, null, null, false);
    }

    public LevelObject create(Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        LevelObject lo = supply(level, rect);
        lo.collide = collide;
        return lo;
    }

    abstract LevelObject supply(Level level, Rectangle rect);

}
