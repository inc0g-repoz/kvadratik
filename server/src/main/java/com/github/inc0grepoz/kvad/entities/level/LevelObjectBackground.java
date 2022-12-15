package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, Color color) {
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
