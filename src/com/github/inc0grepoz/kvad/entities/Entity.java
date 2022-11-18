package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Entity {

    private final Rectangle rect, collider;
    private final Level level;
    private boolean collide;
    private int moveSpeed;

    public Entity(Rectangle rect, Level level) {
        this.rect = rect;
        this.collider = null;
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

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public boolean canMove(int x, int y) {
        if (isCollidable()) {
            int nextMinX = (int) getRectangle().getMinX() + x;
            int nextMinY = (int) getRectangle().getMinY() + y;
            int nextMaxX = (int) getRectangle().getMaxX() + x;
            int nextMaxY = (int) getRectangle().getMaxY() + y;
            return level.entitiesStream().filter(e -> e != this && e.isCollidable())
                    .noneMatch(e -> e.getRectangle()
                            .intersects(nextMinX, nextMinY, nextMaxX, nextMaxY));
        } else {
            return true;
        }
    }

    public boolean canMove(Way way, int speed) {
        return canMove(way.x * speed, way.y * speed);
    }

    public boolean canMove(Way way) {
        return canMove(way, moveSpeed);
    }

    public boolean move(int x, int y) {
        boolean moved = canMove(x, y);
        if (moved) {
            rect.x += x;
            rect.y += y;

            if (collider != null) {
                collider.x += x;
                collider.y += y;
            }
        }
        return moved;
    }

    public boolean move(Way way, int speed) {
        return move(way.x * speed, way.y * speed);
    }

    public boolean move(Way way) {
        return move(way, moveSpeed);
    }

    public void teleport(int x, int y) {
        rect.x = x;
        rect.y = y;
        collider.x = x;
        collider.y = y;
        // TODO: Collider offset
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Rectangle getCollider() {
        return collider == null ? rect : collider;
    }

    public Level getLevel() {
        return level;
    }

}
