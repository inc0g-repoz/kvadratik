package com.github.inc0grepoz.kvad.entities.being;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeingTemplate {

    private @Getter String type;
    private Dimension size, colliderSize;
    private Vector2D colliderOffset;

    public Being create(Level level, Point point, int id) {
        Rectangle rect = new Rectangle(point, size);
        Being being = new Being(level, rect, colliderSize, colliderOffset, type, id);
        level.getBeings().add(being);
        return being;
    }

    public Being create(Level level, Point point) {
        return create(level, point, -1);
    }

}
