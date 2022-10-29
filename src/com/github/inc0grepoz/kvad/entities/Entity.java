package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Entity {

    private final Rectangle rect;
    private final Level level;
    private boolean collide;

    public Entity(Rectangle rect, Level level) {
        this.rect = rect;
        this.level = level;
    }

    public Entity(int[] rect, Level level) {
        this(new Rectangle(rect[0], rect[1], rect[2], rect[3]), level);
    }

    public boolean isCollidable() {
        return collide;
    }

    public void setCollidable(boolean collide) {
        this.collide = collide;
    }

    public boolean move(int x, int y) {
        boolean moved;
        if (isCollidable()) {
            int nextMinX = (int) rect.getMinX() + x;
            int nextMinY = (int) rect.getMinY() + y;
            int nextMaxX = (int) rect.getMaxX() + x;
            int nextMaxY = (int) rect.getMaxY() + y;
            moved = level.entitiesStream().filter(e -> e != this && isCollidable())
                    .noneMatch(e -> e.getRectangle()
                            .intersects(nextMinX, nextMinY, nextMaxX, nextMaxY));
        } else {
            moved = true;
        }
        if (moved) {
            rect.x += x;
            rect.y += y;
        }
        return moved;
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
