package com.github.inc0grepoz.kvad.utils;

public class Vector {

    public int x, y;

    public Vector() {}

    public Vector(int x, int y) {
        set(x, y);
    }

    public Vector set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector multiply(int by) {
        x *= by;
        y *= by;
        return this;
    }

    public Vector divide(int by) {
        x /= by;
        y /= by;
        return this;
    }

    public Vector clone() {
        return new Vector(x, y);
    }

}
