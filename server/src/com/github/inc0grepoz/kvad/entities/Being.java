package com.github.inc0grepoz.kvad.entities;

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

    public Being(Level level, int[] rect, int[] coll, BeingType type) {
        super(level, rect, coll);
        super.collide = true;
        super.moveSpeed = 4;
        this.type = type;
    }

    public Being(Level level, int[] rect, BeingType type) {
        this(level, rect, null, type);
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
