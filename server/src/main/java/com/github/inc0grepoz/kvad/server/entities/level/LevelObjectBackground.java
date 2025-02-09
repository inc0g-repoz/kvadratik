package com.github.inc0grepoz.kvad.server.entities.level;

import java.awt.Color;
import java.util.Map;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnimated;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, Color color) {
        super(level, rect, collSize, collOffset, LevelObjectAnim.COLOR);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Animated");
        if (color == null) {
            map.put("anim", getAnim().toString());
        } else {
            String strCol = new StringBuilder()
                    .append(color.getRed())
                    .append(",")
                    .append(color.getGreen())
                    .append(",")
                    .append(color.getBlue())
                    .toString();
            map.put("color", strCol);
        }
    }

}
