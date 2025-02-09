package com.github.inc0grepoz.kvad.server.entities.level;

import java.util.Map;

import com.github.inc0grepoz.kvad.common.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.common.utils.Vector2D;

public class LevelObjectAnimated extends LevelObject{

    private LevelObjectAnim anim;

    public LevelObjectAnimated(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset,
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
