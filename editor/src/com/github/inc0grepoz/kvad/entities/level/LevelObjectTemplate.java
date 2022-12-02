package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;

import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class LevelObjectTemplate {

    private final String name;

    final Dimension size, collSize;
    final Vector collOffset;
    final boolean collide;

    public LevelObjectTemplate(String name, Dimension size,
            Dimension collSize, Vector collOffset, boolean collide) {
        this.name = name;
        this.size = size;
        this.collSize = collSize;
        this.collOffset = collOffset;
        this.collide = collide;
    }

    public LevelObjectTemplate(String type, Dimension size) {
        this(type, size, null, null, false);
    }

    public String getName() {
        return name;
    }

    public LevelObject create(Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        LevelObject lo = supply(level, rect);
        lo.collide = collide;
        return lo;
    }

    public Icon getListIcon() {
        return null;
    }

    abstract LevelObject supply(Level level, Rectangle rect);

}
