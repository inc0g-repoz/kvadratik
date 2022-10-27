package com.github.inc0grepoz.kvad.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.level.Level;

public class Being extends Renderable {

    private final BeingType type;

    private Anim anim = Anim.IDLE_S;
    private long animExpiry; // 0 for infinite duration
    private int animSpriteIndex;

    public Being(Rectangle rect, Level level, BeingType type) {
        super(rect, level);
        this.type = type;
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
        BufferedImage[] images = anim.getImages(type);
        if (System.currentTimeMillis() > animExpiry) {
            animSpriteIndex += images.length < animSpriteIndex + 2 ?
                    -animSpriteIndex : 1;
            animExpiry = System.currentTimeMillis() + anim.getDelay();
        }
        return images[animSpriteIndex];
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(getSprite(), x, y, getLevel().getGame().getCanvas());
    }

}
