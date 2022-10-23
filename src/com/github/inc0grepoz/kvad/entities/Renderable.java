package com.github.inc0grepoz.kvad.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Renderable extends Entity {

    public Renderable(Rectangle rect) {
        super(rect);
    }

    public void draw(Graphics graphics, Camera camera) {
        Rectangle ent = getRectangle();
        Rectangle cam = camera.getRectangle();
        graphics.drawRect(ent.x - cam.x, ent.y - cam.y, ent.width, ent.height);
    }

}
