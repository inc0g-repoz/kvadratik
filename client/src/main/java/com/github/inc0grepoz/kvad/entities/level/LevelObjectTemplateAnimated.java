package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
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
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectAnimated(level, rect, collSize, collOffset, anim);
    }

}
