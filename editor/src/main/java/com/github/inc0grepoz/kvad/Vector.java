package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.entities.being.Anim.Way;

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

    public Way toWay() {
        Way[] ways = Way.values();
        for (int i = 0; i < ways.length; i++) {
            if (compareDigits(ways[i].x, x)
                    && compareDigits(ways[i].y, y)) {
                return ways[i];
            }
        }
        return null;
    }

    private boolean compareDigits(int a, int b) {
        return a > 0 && b > 0
                || a == 0 && b == 0
                || a < 0 && b < 0;
    }

}
