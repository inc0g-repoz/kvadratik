package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class Renderable extends Entity {

    public boolean selected = true;

    public Renderable(Level level, Rectangle rect, Dimension collSize, Vector collOffset) {
        super(level, rect, collSize, collOffset);
    }

    public int getRenderPriority() {
        return (int) getCollider().getMinY();
    }

    public boolean render(Graphics gfx, Camera camera) {
        Rectangle ent = getRectangle();
        Rectangle cam = camera.getRectangle();

        if (!cam.intersects(ent)) {
            return false;
        }

        int x = ent.x - cam.x, y = ent.y - cam.y;
        draw(gfx, x, y, ent.width, ent.height);

        // Drawing the collider
        if (selected) {
            if (collide) {
                int entX = ent.x - cam.x, entY = ent.y - cam.y;
                drawOutline(gfx, entX, entY, ent);
            }
            typeText(gfx, cam, ent);
        }

        return true;
    }

    public void draw(Graphics gfx, int x, int y, int width, int height) {
        gfx.drawRect(x, y, width, height);
    }

    public void drawOutline(Graphics gfx, int x, int y, Rectangle col) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        gfx.drawRect(x, y, col.width, col.height);
        gfx.setColor(color);
    }

    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {}

}
