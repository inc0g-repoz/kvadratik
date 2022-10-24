package com.github.inc0grepoz.kvad.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;

public abstract class Renderable extends Entity {

    public Renderable(Rectangle rect, Level level) {
        super(rect, level);
    }

    public void draw(Graphics graphics, Camera camera) {
        Rectangle ent = getRectangle();
        Rectangle cam = camera.getRectangle();
        graphics.drawRect(ent.x - cam.x, ent.y - cam.y, ent.width, ent.height);
    }

}
