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

    public boolean isColliding(Entity entity) {
        return isCollidable() && entity.isCollidable()
                && getRectangle().intersects(entity.getRectangle());
    }

    public boolean isCollidable() {
        return collide;
    }

    public void setCollidable(boolean collide) {
        this.collide = collide;
    }

    public boolean move(int x, int y) {
        rect.x += x;
        rect.y += y;
        boolean moved = level.getGame().getLevel().entitiesStream()
                .filter(e -> e != this).noneMatch(this::isColliding);
        if (!moved) {
            rect.x -= x;
            rect.y -= y;
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
