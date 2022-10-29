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

    public Being(int[] rect, Level level, BeingType type) {
        super(rect, level);
        this.type = type;
        setCollidable(true);
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
        graphics.drawImage(getSprite(), x, y, width, height, getLevel().getGame().getCanvas());
    }

}
