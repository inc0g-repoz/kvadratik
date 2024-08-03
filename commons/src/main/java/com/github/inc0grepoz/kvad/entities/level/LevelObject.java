package com.github.inc0grepoz.kvad.entities.level;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;

public abstract class LevelObject extends Renderable {

    @Getter
    private String name;

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset) {
        super(level, rect, collSize, collOffset);
    }

    @Override
    public void delete() {
        getLevel().getLevelObjects().remove(this);
    }

}
