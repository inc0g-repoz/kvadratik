package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LevelObjectTemplate {

    @Getter String name;

    LevelObjectType type;
    Dimension size, colliderSize;
    Vector2D colliderOffset;
    boolean collide;
    int[] color;

    public LevelObject create(Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        LevelObject lo = supply(level, rect);
        lo.collide = collide;
        return lo;
    }

    LevelObject supply(Level level, Rectangle rect) {
        return type.getFactory().supply(this, level, rect);
    }

}
