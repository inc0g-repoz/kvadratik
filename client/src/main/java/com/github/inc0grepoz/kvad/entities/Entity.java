package com.github.inc0grepoz.kvad.entities;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

import lombok.Getter;

public abstract class Entity {

    public boolean collide, move;
    public int speed;

    private final @Getter Level level;
    private final Rectangle rect, coll;
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

    public boolean canMove(double x, double y) {
        if (!collide) {
            return true;
        }

        Rectangle coll = getCollider();
        double nextMinX = coll.x + x, nextMinY = coll.y + y;
        double nextMaxX = coll.width + x, nextMaxY = coll.height + y;
        return level.renEntsStream().filter(e -> e != this && e.collide)
                .noneMatch(e -> e.getCollider().intersects(nextMinX, nextMinY, nextMaxX, nextMaxY));
    }

    public boolean canMove(Way way, int speed) {
        return way != null && canMove(way.x * speed, way.y * speed);
    }

    public boolean canMove(Way way) {
        return canMove(way, speed);
    }

    public boolean move(double x, double y) {
        boolean moved = (x != 0 || y != 0) && canMove(x, y);
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

    public boolean move(Way way, double speed) {
        return way != null && speed != 0 && move(way.x * speed, way.y * speed);
    }

    public boolean move(Way way) {
        return move(way, speed);
    }

    public void teleport(double x, double y) {
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

}
