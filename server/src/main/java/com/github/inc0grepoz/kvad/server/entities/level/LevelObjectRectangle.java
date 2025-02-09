package com.github.inc0grepoz.kvad.server.entities.level;

import java.util.Map;

import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;

public class LevelObjectRectangle extends LevelObject {

    public LevelObjectRectangle(Level level, Rectangle rect) {
        super(level, rect, null, null);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Rectangle");
    }

}
