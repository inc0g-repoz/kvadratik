package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectTemplateAnimated extends LevelObjectTemplate {

    final LevelObjectAnim anim;

    public LevelObjectTemplateAnimated(String type, Dimension size,
            Dimension collSize, Vector collOffset, boolean collide,
            LevelObjectAnim anim) {
        super(type, size, collSize, collOffset, collide);
        this.anim = anim;
    }

    @Override
    public Icon getListIcon() {
        if (anim != null) {
            Image[] images = anim.getImages();
            if (images != null && images.length != 0) {
                return new ImageIcon(images[0]);
            }
        }
        return null;
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectAnimated(level, rect, collSize, collOffset, anim, getType());
    }

}
