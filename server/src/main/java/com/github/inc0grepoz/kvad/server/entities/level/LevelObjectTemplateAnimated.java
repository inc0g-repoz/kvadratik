package com.github.inc0grepoz.kvad.server.entities.level;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnimated;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;

public class LevelObjectTemplateAnimated extends LevelObjectTemplate {

    final LevelObjectAnim anim;

    public LevelObjectTemplateAnimated(String type, Dimension size,
            Dimension collSize, Vector2D collOffset, boolean collide,
            LevelObjectAnim anim) {
        super(type, size, collSize, collOffset, collide);
        this.anim = anim;
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectAnimated(level, rect, collSize, collOffset, anim);
    }

}
