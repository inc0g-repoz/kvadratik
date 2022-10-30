package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.KvadratikCanvas;
import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Renderable extends Entity {

    public Renderable(Rectangle rect, Level level) {
        super(rect, level);
    }

    public Renderable(int[] rect, Level level) {
        super(rect, level);
    }

    public int getOffsetX() {
        return getRectangle().x - getLevel().getCamera().getRectangle().x;
    }

    public int getOffsetY() {
        return getRectangle().y - getLevel().getCamera().getRectangle().y;
    }

    public void render(Graphics gfx) {
        Rectangle ent = getRectangle();
        int x = getOffsetX();
        int y = getOffsetY();
        draw(gfx, x, y, ent.width, ent.height);

        // Drawing the collider
        if (isCollidable()) {
            KvadratikCanvas canvas = getLevel().getGame().getCanvas();
            if (canvas.isDrawCollidersEnabled()) {
                drawCollider(gfx, x, y, ent);
            }
        }
    }

    public void draw(Graphics gfx, int x, int y, int width, int height) {
        gfx.drawRect(x, y, width, height);
    }

    public void drawCollider(Graphics gfx, int x, int y, Rectangle rect) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        int halfWidth = rect.width / 2;
        gfx.drawLine(x + halfWidth, y, x + halfWidth, y + rect.height);
        gfx.drawRect(x, y, rect.width, rect.height);
        gfx.setColor(color);
    }

}
