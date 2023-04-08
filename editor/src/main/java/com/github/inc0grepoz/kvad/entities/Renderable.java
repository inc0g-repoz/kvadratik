package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public abstract class Renderable extends Entity {

    public boolean selected;

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

        int x = (int) (ent.x - cam.x), y = (int) (ent.y - cam.y);
        int width = (int) ent.width, height = (int) ent.height;
        draw(gfx, x, y, width, height);

        // Drawing the collider
        if (selected) {
            if (collide) {
                drawOutline(gfx, x, y, width, height);
            }
            typeText(gfx, cam, ent);
        }

        return true;
    }

    public void draw(Graphics gfx, int x, int y, int width, int height) {
        gfx.drawRect(x, y, width, height);
    }

    public void drawOutline(Graphics gfx, int x, int y, int width, int height) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        gfx.drawRect(x, y, width, height);
        gfx.setColor(color);
    }

    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {}

    public abstract void delete();

}
