package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public class BeingTemplate {

    private final String type;
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

    public String getType() {
        return type;
    }

    public Being create(Level level, Point point, int id) {
        Rectangle rect = new Rectangle(point, size);
        return new Being(level, rect, collSize, collOffset, type, id);
    }

    public Being create(Level level, Point point) {
        return create(level, point, -1);
    }

}