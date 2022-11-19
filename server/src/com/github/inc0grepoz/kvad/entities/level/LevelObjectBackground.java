package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.util.Map;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, int[] rect, LevelObjectAnim anim) {
        super(level, rect, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, int[] rect, Color color) {
        super(level, rect, LevelObjectAnim.COLOR);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Animated");
        if (color == null) {
            map.put("anim", getAnim().name());
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
