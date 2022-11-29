package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class LevelObject extends Renderable {

    private final String type;

    public LevelObject(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset,
            String type) {
        super(level, rect, collSize, collOffset);
        this.type = type;
    }

    public String getName() {
        return type;
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        int x = (int) (rect.getCenterX() - cam.x), y = rect.y + rect.height - cam.y;
        int width = gfx.getFontMetrics().stringWidth(type);
        x -= width / 2;
        y += 15;
        gfx.drawString(type, x, y);
    }

}
