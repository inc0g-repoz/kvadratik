package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Entity {

    private final Rectangle rect;
    private final Level level;

    public Entity(Rectangle rect, Level level) {
        this.rect = rect;
        this.level = level;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Level getLevel() {
        return level;
    }

}
