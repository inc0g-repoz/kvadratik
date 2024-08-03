package com.github.inc0grepoz.kvad.entities.factory;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public interface RenderableTemplate {

    String getType();

    Dimension getSize();

    Renderable create(Level level, Point point);

}
