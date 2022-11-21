package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Entity {

    private static Rectangle rect(int[] arr) {
        return arr == null || arr.length != 4 ? null
                : new Rectangle(arr[0], arr[1], arr[2], arr[3]);
    }

    public boolean collide;
    public int moveSpeed;

    private final Rectangle rect, coll;
    private final Level level;

    public Entity(Level level, int[] rect, int[] coll) {
        this.level = level;
        this.rect = rect(rect);
        this.coll = rect(coll);
    }

    public Entity(Level level, int[] rect) {
        this(level, rect, null);
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

    public void move(int x, int y) {
        rect.x += x;
        rect.y += y;
    }

    public void move(Way way, int speed) {
        move(way.x * speed, way.y * speed);
    }

    public void move(Way way) {
        move(way, moveSpeed);
    }

    public void teleport(int x, int y) {
        rect.x = x;
        rect.y = y;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Rectangle getCollider() {
        return coll;
    }

    public boolean hasCollider() {
        return coll != null;
    }

    public Level getLevel() {
        return level;
    }

    protected abstract void packetEntries(Map<String, String> map);

}
