package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;

public class LevelObjectTemplateRectangle extends LevelObjectTemplate {

    public LevelObjectTemplateRectangle(String name, Dimension size) {
        super(name, size);
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectRectangle(level, rect, getType());
    }

}
