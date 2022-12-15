package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;

import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectAnimated extends LevelObject{

    private LevelObjectAnim anim;

    public LevelObjectAnimated(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset,
            LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset);
        this.anim = anim;
    }

    public LevelObjectAnim getAnim() {
        return anim;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Animated");
        map.put("anim", anim.toString());
    }

}
