package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Rectangle;

public class LevelObjectTemplateRectangle extends LevelObjectTemplate {

    public LevelObjectTemplateRectangle(String name, Dimension size) {
        super(name, size);
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectRectangle(level, rect, getName());
    }

}
