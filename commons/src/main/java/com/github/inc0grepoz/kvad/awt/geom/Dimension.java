package com.github.inc0grepoz.kvad.awt.geom;

import java.awt.geom.Dimension2D;

public class Dimension extends Dimension2D {

    public double width, height;

    public Dimension(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

}
