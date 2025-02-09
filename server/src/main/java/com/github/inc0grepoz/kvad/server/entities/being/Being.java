package com.github.inc0grepoz.kvad.server.entities.being;

import java.util.Map;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.Entity;
import com.github.inc0grepoz.kvad.common.entities.being.Anim;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

import lombok.Getter;

public class Being extends Entity {

    private static int lastId;

    private final @Getter int id = ++lastId;
    private @Getter String type;
    private @Getter Anim anim = Anim.IDLE_S;

    public Being(Level level, Rectangle rect, Dimension collSize, Vector2D collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        super.collide = true;
        this.type = type;
    }

    public void moveOn(long timeGap) {
        double moveSpeed = anim.speed * timeGap;
        if (anim.way.x != 0 || anim.way.y != 0) {
            double moveX = anim.way.x * moveSpeed;
            double moveY = anim.way.y * moveSpeed;
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
    public void delete() {
        KvadratikServer.INSTANCE.packetUtil.outBeingDespawn(this);
        getLevel().getBeings().removeIf(b -> b.getId() == id);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", type);
        map.put("id", String.valueOf(id));
    }

}
