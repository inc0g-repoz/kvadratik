package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.entities.Renderable;

public abstract class LevelObject extends Renderable {

    public LevelObject(Level level, int[] rect, int[] coll) {
        super(level, rect, coll);
    }

    public LevelObject(Level level, int[] rect) {
        super(level, rect);
    }

}
