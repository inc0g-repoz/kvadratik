package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelObjectAnimated extends LevelObject{

    private LevelObjectAnim type;
    private long stateExpiry; // 0 for infinite duration
    private int stateSpriteIndex;

    public LevelObjectAnimated(int[] rect, Level level, LevelObjectAnim type) {
        super(rect, level);
        this.type = type;
        setCollidable(true);
    }

    public BufferedImage getSprite() {
        BufferedImage[] images = type.getImages();
        if (System.currentTimeMillis() > stateExpiry) {
            stateSpriteIndex += images.length < stateSpriteIndex + 2 ?
                    -stateSpriteIndex : 1;
            stateExpiry = System.currentTimeMillis() + type.getDelay();
        }
        return images[stateSpriteIndex];
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(getSprite(), x, y, width, height, getLevel().getGame().getCanvas());
    }

}
