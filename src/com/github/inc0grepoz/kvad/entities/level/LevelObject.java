package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.entities.Renderable;

public abstract class LevelObject extends Renderable {

    private static final long serialVersionUID = -3073854085950464252L;

    public LevelObject(int[] rect, Level level) {
        super(rect, level);
    }

}
