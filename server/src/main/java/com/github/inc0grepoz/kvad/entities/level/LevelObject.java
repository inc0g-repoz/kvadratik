package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class LevelObject extends Entity {

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset) {
        super(level, rect, collSize, collOffset);
    }

}
