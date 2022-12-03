package com.github.inc0grepoz.kvad.entities.factory;

import java.awt.Point;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public interface RenderableFactory {

    Renderable create(String type, Level level, Point point);

}
