package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Entity;

public abstract class LevelObject extends Entity {

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset) {
        super(level, rect, collSize, collOffset);
    }

    @Override
    public void delete() {
        getLevel().getLevelObjects().remove(this);
    }

}
