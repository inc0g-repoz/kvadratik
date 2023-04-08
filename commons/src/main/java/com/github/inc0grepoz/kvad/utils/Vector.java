package com.github.inc0grepoz.kvad.utils;

public class Vector {

    public double x, y;

    public Vector() {}

    public Vector(double x, double y) {
        set(x, y);
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector multiply(double by) {
        x *= by;
        y *= by;
        return this;
    }

    public Vector divide(double by) {
        x /= by;
        y /= by;
        return this;
    }

    public Vector clone() {
        return new Vector(x, y);
    }

}
