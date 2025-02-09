package com.github.inc0grepoz.kvad.server.entities.level;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectRectangle;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectTemplate;

public class LevelObjectTemplateRectangle extends LevelObjectTemplate {

    public LevelObjectTemplateRectangle(String type, Dimension size) {
        super(type, size);
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return new LevelObjectRectangle(level, rect);
    }

}
