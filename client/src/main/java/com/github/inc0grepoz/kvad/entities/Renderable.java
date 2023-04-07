package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Renderable extends Entity {

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

        int x = (int) (ent.getX() - cam.getX()), y = (int) (ent.y - cam.y);
        draw(gfx, x, y, (int) ent.width, (int) ent.height);

        // Drawing the collider
        if (collide) {
            KvadratikCanvas canvas = KvadratikGame.INSTANCE.getCanvas();
            if (canvas.drawColliders) {
                Rectangle coll = getCollider();
                int collX = (int) (coll.x - cam.x), collY = (int) (coll.y - cam.y);
                drawCollider(gfx, collX, collY, (int) coll.width, (int) coll.height);
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

    public void drawCollider(Graphics gfx, int x, int y, int width, int height) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        gfx.drawRect(x, y, width, height);
        gfx.setColor(color);
    }

    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {}

}
