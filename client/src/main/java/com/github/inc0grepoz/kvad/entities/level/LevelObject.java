package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Vector2D;

public abstract class LevelObject extends Renderable {

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset) {
        super(level, rect, collSize, collOffset);
    }

}
