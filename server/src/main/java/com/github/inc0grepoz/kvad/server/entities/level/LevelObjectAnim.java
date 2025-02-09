package com.github.inc0grepoz.kvad.server.entities.level;

import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnim;

public class LevelObjectAnim {

    public static final LevelObjectAnim COLOR = new LevelObjectAnim("color");

    private static LevelObjectAnim[] anims;

    public static void init(LevelObjectAnim[] anims) {
        if (LevelObjectAnim.anims != null) {
            throw new IllegalStateException("Being types already have been initialized");
        }
        LevelObjectAnim.anims = anims;
    }

    public static LevelObjectAnim valueOf(String name) {
        for (int i = 0; i < anims.length; i++) {
            if (anims[i].name.equals(name)) {
                return anims[i];
            }
        }
        return null;
    }

    public static LevelObjectAnim[] values() {
        return anims;
    }

    private final String name;

    public LevelObjectAnim(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
