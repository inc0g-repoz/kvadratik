package com.github.inc0grepoz.kvad.entities.being;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
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

    public Being create(Level level, Point point, int id) {
        Rectangle rect = new Rectangle(point, size);
        Being being = new Being(level, rect, collSize, collOffset, type, id);
        level.getBeings().add(being);
        return being;
    }

    public Being create(Level level, Point point) {
        return create(level, point, -1);
    }

}
