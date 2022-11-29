package com.github.inc0grepoz.kvad.entities;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class Entity {

    public boolean collide, move;
    public int speed;

    private final Rectangle rect, coll;
    private final Level level;
    private final Vector collOffset;

    public Entity(Level level, Rectangle rect, Dimension collSize, Vector collOffset) {
        this.level = level;
        this.rect = rect;

        if (collSize != null && collOffset != null) {
            this.collOffset = collOffset;
            coll = new Rectangle(collSize);
            coll.x += rect.x + collOffset.x;
            coll.y = rect.y + collOffset.y;
        } else {
            this.collOffset = null;
            coll = null;
        }
    }

    public boolean canMove(int x, int y) {
        if (collide) {
            Rectangle coll = getCollider();
            int nextMinX = coll.x + x, nextMinY = coll.y + y;
            int nextMaxX = coll.width + x, nextMaxY = coll.height + y;
            return level.renEntsStream().filter(e -> e != this && e.collide)
                    .noneMatch(e -> e.getCollider().intersects(nextMinX, nextMinY, nextMaxX, nextMaxY));
        } else {
            return true;
        }
    }

    public boolean canMove(Way way, int speed) {
        return way != null && canMove(way.x * speed, way.y * speed);
    }

    public boolean canMove(Way way) {
        return canMove(way, speed);
    }

    public boolean move(int x, int y) {
        boolean moved = canMove(x, y);
        if (moved) {
            rect.x += x;
            rect.y += y;

            if (coll != null) {
                coll.x += x;
                coll.y += y;
            }
        }
        return moved;
    }

    public boolean move(Way way, int speed) {
        return way != null && move(way.x * speed, way.y * speed);
    }

    public boolean move(Way way) {
        return move(way, speed);
    }

    public void teleport(int x, int y) {
        rect.x = x;
        rect.y = y;

        if (coll != null && collOffset != null) {
            coll.x = x + collOffset.x;
            coll.y = y + collOffset.y;
        }
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Rectangle getCollider() {
        return coll == null ? rect : coll;
    }

    public Level getLevel() {
        return level;
    }

}
