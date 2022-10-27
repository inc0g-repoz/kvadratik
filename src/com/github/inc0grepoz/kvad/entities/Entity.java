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

    public void move(int x, int y) {
        rect.x += x;
        rect.y += y;
    }

    public void teleport(int x, int y) {
        rect.x = x;
        rect.y = y;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Level getLevel() {
        return level;
    }

}
