package com.github.inc0grepoz.kvad;

import javax.naming.OperationNotSupportedException;

import com.github.inc0grepoz.commons.util.json.mapper.JsonMapper;
import com.github.inc0grepoz.kvad.awt.Canvas;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;

public interface Kvadratik {

    AssetsProvider getAssetsProvider();

    JsonMapper getJsonMapper();

    default Canvas getCanvas() {
        throw new RuntimeException(new OperationNotSupportedException());
    }

    default int getWidth() {
        return 0;
    }

    default int getHeight() {
        return 0;
    }

    default boolean isDrawingColliders() {
        return false;
    }

    default void move(Being being, double moveX, double moveY) {}

    default void sendAnim(Being being) {}

    BeingFactory getBeingFactory();

    LevelObjectFactory getLevelObjectFactory();

}
