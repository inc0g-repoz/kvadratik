package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

import lombok.Getter;

public class Being extends Entity {

    private static int lastId;

    private final @Getter int id = ++lastId;
    private @Getter String type;
    private @Getter Anim anim = Anim.IDLE_S;

    public Being(Level level, Rectangle rect, Dimension collSize, Vector collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        super.collide = true;
        this.type = type;
    }

    public void moveOn() {
        if (anim.way.x != 0 || anim.way.y != 0) {
            int moveX = anim.way.x * anim.speed;
            int moveY = anim.way.y * anim.speed;
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

    @Override
    public void teleport(int x, int y) {
        super.teleport(x, y);
        getLevel().getServer().packetUtil.outBeingTeleport(this);
    }

    @Override
    public void delete() {
        getLevel().getBeings().removeIf(b -> b.getId() == id);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", type);
        map.put("id", String.valueOf(id));
    }

}
