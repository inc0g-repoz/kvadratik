package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Entity {

    private final Rectangle rect;
    private final Level level;
    private boolean collide;

    public Entity(Level level, Rectangle rect) {
        this.level = level;
        this.rect = rect;
    }

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<>();
        String rString = new StringBuilder()
                .append(rect.x)
                .append(",")
                .append(rect.y)
                .append(",")
                .append(rect.width)
                .append(",")
                .append(rect.height)
                .toString();
        map.put("rect", rString);
        map.put("level", level.getName());
        map.put("collide", Boolean.toString(collide));

        // Getting some extra data from the child class instance
        packetEntries(map);

        // Joining the elements
        StringJoiner sjEntity = new StringJoiner(";");
        map.forEach((k, v) -> sjEntity.add(k + "=" + v));
        return sjEntity.toString();
    }

    public Entity(Level level, int[] rect) {
        this(level, new Rectangle(rect[0], rect[1], rect[2], rect[3]));
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

    protected abstract void packetEntries(Map<String, String> map);

}
