package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.entities.Renderable;

import lombok.Getter;

public abstract class LevelObject extends Renderable {

    private final @Getter String type;

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        this.type = type;
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        int x = (int) (rect.getCenterX() - cam.x), y = rect.y + rect.height - cam.y;
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
