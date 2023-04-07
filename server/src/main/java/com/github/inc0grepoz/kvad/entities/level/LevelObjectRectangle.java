package com.github.inc0grepoz.kvad.entities.level;

import java.util.Map;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;

public class LevelObjectRectangle extends LevelObject {

    public LevelObjectRectangle(Level level, Rectangle rect) {
        super(level, rect, null, null);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Rectangle");
    }

}
