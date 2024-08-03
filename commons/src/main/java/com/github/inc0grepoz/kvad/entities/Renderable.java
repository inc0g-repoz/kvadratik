package com.github.inc0grepoz.kvad.entities;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Platform;
import com.github.inc0grepoz.kvad.utils.Vector2D;

public abstract class Renderable extends Entity {

    public boolean selected;

    public Renderable(Level level, Rectangle rect, Dimension collSize, Vector2D collOffset) {
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
        int eWidth = (int) ent.width, eHeight = (int) ent.height;
        draw(gfx, x, y, eWidth, eHeight);

        // Drawing the collider
        if (collide) {
            Kvadratik kvad = Platform.getInstance();

            if (kvad.isDrawingColliders()) {
                Rectangle coll = getCollider();
                int collX = (int) (coll.x - cam.x), collY = (int) (coll.y - cam.y);
                drawCollider(gfx, collX, collY, (int) coll.width, (int) coll.height);
            }

            if (selected) {
                drawOutline(gfx, x, y, eWidth, eHeight);
                typeText(gfx, cam, ent);
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

    public void drawOutline(Graphics gfx, int x, int y, int width, int height) {
        Color color = gfx.getColor();
        gfx.setColor(Color.GREEN);
        gfx.drawRect(x, y, width, height);
        gfx.setColor(color);
    }

    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {}

    public abstract void delete();

}
