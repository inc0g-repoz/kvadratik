package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectAnimated extends LevelObject{

    private final LevelObjectAnim anim;

    private long stateExpiry; // 0 for infinite duration
    private int stateSpriteIndex;

    public LevelObjectAnimated(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset,
            LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset);
        this.anim = anim;
    }

    public LevelObjectAnim getAnim() {
        return anim;
    }

    public BufferedImage getSprite() {
        if (anim == LevelObjectAnim.COLOR) {
            return null;
        }

        BufferedImage[] images = anim.getImages();
        if (System.currentTimeMillis() > stateExpiry) {
            stateSpriteIndex += images.length < stateSpriteIndex + 2 ?
                    -stateSpriteIndex : 1;
            stateExpiry = System.currentTimeMillis() + anim.getDelay();
        }

        return images[stateSpriteIndex];
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(getSprite(), x, y, width, height, getLevel().getEditor().getCanvas());
    }

}
