package com.github.inc0grepoz.kvad.server.entities.level;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.Entity;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;

public abstract class LevelObject extends Entity {

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset) {
        super(level, rect, collSize, collOffset);
    }

    @Override
    public void delete() {
        getLevel().getLevelObjects().remove(this);
    }

}
