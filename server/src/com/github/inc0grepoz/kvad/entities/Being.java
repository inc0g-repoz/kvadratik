package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public class Being extends Entity {

    private static int lastId;

    private final BeingType type;
    private final int id = ++lastId;
    private final Vector velocity = new Vector();

    private Anim anim = Anim.IDLE_S;
    private int walkSpeed = 4;

    public Being(Level level, int[] rect, BeingType type) {
        super(level, rect);
        this.type = type;
        setCollidable(true);
    }

    public int getId() {
        return id;
    }

    public int getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(int walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    @Override
    public boolean move(int x, int y) {
        Rectangle rect = getRectangle();
        boolean moved;
        if (isCollidable()) {
            int nextX = x + (int) rect.getCenterX();
            int nextY = y + (int) rect.getY() + rect.height;
            moved = getLevel().entitiesStream()
                    .filter(e -> e != this && e.isCollidable())
                    .noneMatch(e -> e.getRectangle().contains(nextX, nextY));
        } else {
            moved = true;
        }
        if (moved) {
            rect.x += x;
            rect.y += y;
        }
        return moved;
    }

    public void applyAnim(Anim anim) {
        if (this.anim != anim) {
            this.anim = anim;
        }
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Anim getAnim() {
        return anim;
    }

    public BeingType getType() {
        return type;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", type.name());
        map.put("id", String.valueOf(id));
    }

}
