package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;

public abstract class LevelObjectTemplate {

    private final @Getter String type;

    final Dimension size, collSize;
    final Vector2D collOffset;
    final boolean collide;

    public LevelObjectTemplate(String type, Dimension size,
            Dimension collSize, Vector2D collOffset, boolean collide) {
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
