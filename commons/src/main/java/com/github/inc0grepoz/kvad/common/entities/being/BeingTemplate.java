package com.github.inc0grepoz.kvad.common.entities.being;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.factory.RenderableTemplate;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeingTemplate implements RenderableTemplate {

    private String name;
    private Dimension size, colliderSize;
    private Vector2D colliderOffset;

    public Being create(Level level, Point point, int id) {
        Rectangle rect = new Rectangle(point, size);
        Being being = new Being(level, rect, colliderSize, colliderOffset, name, id);
        level.getBeings().add(being);
        return being;
    }

    public Being create(Level level, Point point) {
        return create(level, point, -1);
    }

}
