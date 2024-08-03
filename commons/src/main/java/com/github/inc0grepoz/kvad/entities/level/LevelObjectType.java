package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@FunctionalInterface
interface Factory {
    LevelObject supply(LevelObjectTemplate t, Level level, Rectangle rect);
}

@Getter
@RequiredArgsConstructor
public enum LevelObjectType {

    ANIMATED  ((t, l, r) -> {
        return new LevelObjectAnimated(l, r, t.getColliderSize(), t.getColliderOffset(), LevelObjectAnim.valueOf(t.getName()));
    }),
    BACKGROUND((t, l, r) -> {
        int[] color = t.getColor();
        return color == null
             ? new LevelObjectBackground(l, r, t.getColliderSize(), t.getColliderOffset(), LevelObjectAnim.COLOR)
             : new LevelObjectBackground(l, r, t.getColliderSize(), t.getColliderOffset(), new Color(color[0], color[1], color[2]));
    }),
    RECTANGLE ((t, l, r) -> new LevelObjectRectangle(l, r));

    private final Factory factory;

}
