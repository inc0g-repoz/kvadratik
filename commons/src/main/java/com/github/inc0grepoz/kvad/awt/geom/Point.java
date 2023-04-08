package com.github.inc0grepoz.kvad.awt.geom;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double {

    private static final long serialVersionUID = -7015163678446392225L;

    public static Point fromAwtPoint(java.awt.Point point) {
        return new Point(point.x, point.y);
    }

    public Point(double x, double y) {
        super(x, y);
    }

}
