package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectTemplateBackground extends LevelObjectTemplateAnimated {

    private final Color color;

    public LevelObjectTemplateBackground(String name, Dimension size,
            Dimension collSize, Vector collOffset, boolean collide,
            LevelObjectAnim anim) {
        super(name, size, collSize, collOffset, collide, anim);
        this.color = null;
    }

    public LevelObjectTemplateBackground(String name, Dimension size,
            Dimension collSize, Vector collOffset, boolean collide,
            Color color) {
        super(name, size, collSize, collOffset, collide, LevelObjectAnim.COLOR);
        this.color = color;
    }

    @Override
    LevelObject supply(Level level, Rectangle rect) {
        return color == null ? new LevelObjectBackground(level, rect, collSize, collOffset, anim, getName())
                : new LevelObjectBackground(level, rect, collSize, collOffset, color, getName());
    }

}
