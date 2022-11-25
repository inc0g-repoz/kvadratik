package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public class Being extends Entity {

    private static int lastId;

    private final int id = ++lastId;
    private final Vector velocity = new Vector();

    private String type;
    private Anim anim = Anim.IDLE_S;

    public Being(Level level, Rectangle rect, Dimension collSize, Vector collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        super.collide = true;
        super.speed = 4;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void moveOn() {
        if (speed != 0) {
            int moveX = anim.way.x * speed;
            int moveY = anim.way.y * speed;
            move(moveX, moveY);
        }
    }

    public void morph(String type) {
        this.type = type;
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

    public String getType() {
        return type;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", type);
        map.put("id", String.valueOf(id));
    }

}
