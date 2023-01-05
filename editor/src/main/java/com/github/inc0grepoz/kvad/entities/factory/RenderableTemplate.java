package com.github.inc0grepoz.kvad.entities.factory;

import java.awt.Dimension;
import java.awt.Point;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public interface RenderableTemplate {

    String getType();

    Dimension getSize();

    Renderable create(Level level, Point point);

}
