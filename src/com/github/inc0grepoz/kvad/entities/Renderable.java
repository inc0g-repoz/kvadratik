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

    public void render(Graphics gfx, Camera camera) {
        Rectangle ent = getRectangle();
        Rectangle cam = camera.getRectangle();
        int x = ent.x - cam.x;
        int y = ent.y - cam.y;

        /*
        if (!cam.contains(x, y)) {
            return;
        }
        */

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
