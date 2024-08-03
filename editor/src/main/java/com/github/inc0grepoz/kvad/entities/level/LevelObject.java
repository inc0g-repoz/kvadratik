package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;

public abstract class LevelObject extends Renderable {

    private final @Getter String type;

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        this.type = type;
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        int x = (int) (rect.getCenterX() - cam.x), y = (int) (rect.y + rect.height - cam.y);
        int width = gfx.getFontMetrics().stringWidth(type);
        x -= width / 2;
        y += 15;
        gfx.drawString(type, x, y);
    }

    @Override
    public void delete() {
        getLevel().getLevelObjects().remove(this);
    }

}
