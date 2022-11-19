package com.github.inc0grepoz.kvad.entities.level;

import java.util.Map;

public class LevelObjectAnimated extends LevelObject{

    private LevelObjectAnim anim;

    public LevelObjectAnimated(Level level, int[] rect, LevelObjectAnim anim) {
        super(level, rect);
        this.anim = anim;
    }

    public LevelObjectAnim getAnim() {
        return anim;
    }

    @Override
    protected void packetEntries(Map<String, String> map) {
        map.put("type", "Animated");
        map.put("anim", anim.name());
    }

}
