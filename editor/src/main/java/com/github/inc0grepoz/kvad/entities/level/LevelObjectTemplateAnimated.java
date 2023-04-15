package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.utils.Vector2D;

public class LevelObjectTemplateAnimated extends LevelObjectTemplate {

    final LevelObjectAnim anim;

    public LevelObjectTemplateAnimated(String type, Dimension size,
            Dimension collSize, Vector2D collOffset, boolean collide,
            LevelObjectAnim anim) {
        super(type, size, collSize, collOffset, collide);
        this.anim = anim;
    }

    @Override
    public Icon getListIcon() {
        if (anim != null) {
            Image[] images = anim.getImages();
            if (images != null && images.length != 0) {
                Image image = images[0].getScaledInstance(32, 32, Image.SCALE_FAST);
                return new ImageIcon(image);
            }
        }
        return null;
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectAnimated(level, rect, collSize, collOffset, anim, getType());
    }

}
