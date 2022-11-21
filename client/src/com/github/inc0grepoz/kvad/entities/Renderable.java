package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Renderable extends Entity {

    public Renderable(Level level, int[] rect, int coll[]) {
        super(level, rect, coll);
    }

    public Renderable(Level level, int[] rect) {
        super(level, rect);
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
        if (collide) {
            KvadratikCanvas canvas = getLevel().getGame().getCanvas();
            if (canvas.drawColliders) {
                Rectangle coll = getCollider();
                int collX = coll.x - cam.x, collY = coll.y - cam.y;
                drawCollider(gfx, collX, collY, coll);
            }
        }

        /* Mostly used for displaying player names
         * above their heads */
        typeText(gfx, cam, ent);

        return true;
    }

    public void draw(Graphics gfx, int x, int y, int width, int height) {
        gfx.drawRect(x, y, width, height);
    }

    public void drawCollider(Graphics gfx, int x, int y, Rectangle col) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        gfx.drawRect(x, y, col.width, col.height);
        gfx.setColor(color);
    }

    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {}

}
