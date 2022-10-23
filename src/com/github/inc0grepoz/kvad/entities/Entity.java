package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

public abstract class Entity {

    private final Rectangle rect;

    public Entity(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRectangle() {
        return rect;
    }

}
