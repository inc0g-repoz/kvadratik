package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;

public class LivingEntity extends Renderable {

    private final Anim anim = new Anim();

    public LivingEntity(Rectangle rect, Level level) {
        super(rect, level);
    }

    public Anim getAnimation() {
        return anim;
    }

}
