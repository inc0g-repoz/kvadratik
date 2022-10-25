package com.github.inc0grepoz.kvad.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.level.Level;

public class Being extends Renderable {

    private final BeingType type;

    private Anim anim;
    private long animExpiry; // 0 for infinite duration
    private int animSpriteIndex;

    public Being(Rectangle rect, Level level, BeingType type) {
        super(rect, level);
        this.type = type;
        anim = type.getIdleAnim();
    }

    public void applyAnim(Anim anim) {
        if (this.anim != anim) {
            this.anim = anim;
            animSpriteIndex = 0;
            animExpiry = System.currentTimeMillis() + anim.getDelay();
        }
    }

    public Anim getAnim() {
        return anim;
    }

    public BeingType getType() {
        return type;
    }

    public BufferedImage getSprite() {
        if (System.currentTimeMillis() > animExpiry) {
            animSpriteIndex += anim.getImages().length < animSpriteIndex + 2 ?
                    -animSpriteIndex : 1;
            animExpiry = System.currentTimeMillis() + anim.getDelay();
        }
        return anim.getImages()[animSpriteIndex];
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(getSprite(), x, y, getLevel().getGame().getCanvas());
    }

}
