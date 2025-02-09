package com.github.inc0grepoz.kvad.common.awt.geom;

import java.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D.Double {

    private static final long serialVersionUID = 5322575412898196893L;

    public Rectangle(double x, double y, double width, double height) {
        setRect(x, y, width, height);
    }

    public Rectangle(Point point, Dimension size) {
        setRect(point.x, point.y, size.width, size.height);
    }

    public Rectangle(Dimension size) {
        setRect(0, 0, size.width, size.height);
    }

    public Rectangle() {}

    public Point getLocation() {
        return new Point(x, y);
    }

}
