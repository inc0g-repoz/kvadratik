package com.github.inc0grepoz.kvad.entities.level;

import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

import lombok.Getter;

public class LevelObjectAnim {

    public static final LevelObjectAnim COLOR = new LevelObjectAnim(0, "color");

    private static LevelObjectAnim[] values;

    public static void init(LevelObjectAnim[] values) {
        if (LevelObjectAnim.values != null) {
            throw new IllegalStateException("Being types already have been initialized");
        }
        LevelObjectAnim.values = values;
    }

    public static LevelObjectAnim valueOf(String name) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].name.equals(name)) {
                return values[i];
            }
        }
        return null;
    }

    public static LevelObjectAnim[] values() {
        return values;
    }

    private final String name;
    private final @Getter BufferedImage[] images;
    private final @Getter long delay;

    public LevelObjectAnim(long delay, String name, String... paths) {
        this.delay = delay;
        this.name = name;
        images = new BufferedImage[paths.length];

        for (int i = 0; i < images.length; i++) {
            String btPath = "assets/objects/sprites/" + this + "/" + paths[i];
            images[i] = KvadratikEditor.ASSETS.image(btPath);
        }
    }

    public String toString() {
        return name.toLowerCase();
    }

}
