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

    public void render(Graphics graphics, Camera camera) {
        Rectangle ent = getRectangle();
        Rectangle cam = camera.getRectangle();
        int x = ent.x - cam.x, y = ent.y - cam.y;
        draw(graphics, x, y, ent.width, ent.height);

        // Drawing the collider
        if (isCollidable()) {
            KvadratikCanvas canvas = getLevel().getGame().getCanvas();
            if (canvas.isDrawCollidersEnabled()) {
                Color color = graphics.getColor();
                graphics.setColor(Color.GREEN);
                graphics.drawRect(x, y, ent.width, ent.height);
                graphics.setColor(color);
            }
        }
    }

    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
    }

}
