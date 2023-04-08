package com.github.inc0grepoz.kvad.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;

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

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<>();
        String pString = new StringBuilder()
                .append(rect.x)
                .append(",")
                .append(rect.y)
                .toString();
        map.put("point", pString);
        map.put("level", level.getName());
        map.put("collide", Boolean.toString(collide));

        // Getting some extra data from the child class instance
        packetEntries(map);

        // Joining the elements
        StringJoiner sjEntity = new StringJoiner(";");
        map.forEach((k, v) -> sjEntity.add(k + "=" + v));
        return sjEntity.toString();
    }

    public boolean move(double x, double y) {
        rect.x += x;
        rect.y += y;

        if (coll != null) {
            coll.x += x;
            coll.y += y;
        }

        return true;
    }

    public boolean move(Way way, double speed) {
        return way != null && move(way.x * speed, way.y * speed);
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

    public abstract void delete();

    protected abstract void packetEntries(Map<String, String> map);

}
