package com.github.inc0grepoz.kvad.entities.factory;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public interface RenderableFactory {

    Renderable create(String type, Level level, Point point);

    RenderableTemplate getTemplate(String type);

}
