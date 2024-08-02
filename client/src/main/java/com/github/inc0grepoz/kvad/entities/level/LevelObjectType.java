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
        return new LevelObjectAnimated(l, r, t.colliderSize, t.colliderOffset, LevelObjectAnim.valueOf(t.name));
    }),
    BACKGROUND((t, l, r) -> {
        return t.color == null
             ? new LevelObjectBackground(l, r, t.colliderSize, t.colliderOffset, LevelObjectAnim.COLOR)
             : new LevelObjectBackground(l, r, t.colliderSize, t.colliderOffset, new Color(t.color[0], t.color[1], t.color[2]));
    }),
    RECTANGLE ((t, l, r) -> new LevelObjectRectangle(l, r));

    private final Factory factory;

}
