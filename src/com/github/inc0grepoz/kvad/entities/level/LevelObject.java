package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.entities.Renderable;

public abstract class LevelObject extends Renderable {

    public LevelObject(int[] rect, Level level) {
        super(rect, level);
    }

}
