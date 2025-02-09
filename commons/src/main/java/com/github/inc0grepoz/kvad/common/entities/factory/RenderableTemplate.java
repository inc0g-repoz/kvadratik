package com.github.inc0grepoz.kvad.common.entities.factory;

import javax.swing.Icon;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.entities.Renderable;
import com.github.inc0grepoz.kvad.common.entities.level.Level;

public interface RenderableTemplate {

    String getName();

    Dimension getSize();

    Renderable create(Level level, Point point);

    default Icon getListIcon() {
        return null;
    }

}
