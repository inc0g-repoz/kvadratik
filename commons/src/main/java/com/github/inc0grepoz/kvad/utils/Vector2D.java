package com.github.inc0grepoz.kvad.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Vector2D implements Cloneable {

    public double x, y;

    public Vector2D(double x, double y) {
        set(x, y);
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D multiply(double by) {
        x *= by;
        y *= by;
        return this;
    }

    public Vector2D divide(double by) {
        x /= by;
        y /= by;
        return this;
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

}
