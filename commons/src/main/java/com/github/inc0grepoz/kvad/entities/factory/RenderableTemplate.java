package com.github.inc0grepoz.kvad.entities.factory;

import javax.swing.Icon;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public interface RenderableTemplate {

    String getName();

    Dimension getSize();

    Renderable create(Level level, Point point);

    default Icon getListIcon() {
        return null;
    }

}
