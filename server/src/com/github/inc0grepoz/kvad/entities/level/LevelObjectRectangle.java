package com.github.inc0grepoz.kvad.entities.level;

import java.util.Map;

public class LevelObjectRectangle extends LevelObject {

    public LevelObjectRectangle(Level level, int[] rect) {
        super(level, rect);
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Rectangle");
    }

}
