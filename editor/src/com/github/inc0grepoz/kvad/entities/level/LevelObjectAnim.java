package com.github.inc0grepoz.kvad.entities.level;

import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.editor.KvadratikGame;

public class LevelObjectAnim {

    public static final LevelObjectAnim COLOR = new LevelObjectAnim(0, "color");

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

    private final long delay;
    private final String name;
    private final BufferedImage[] images;

    public LevelObjectAnim(long delay, String name, String... paths) {
        this.delay = delay;
        this.name = name;
        images = new BufferedImage[paths.length];

        for (int i = 0; i < images.length; i++) {
            String btPath = "assets/objects/sprites/" + this + "/" + paths[i];
            images[i] = KvadratikGame.ASSETS.image(btPath);
        }
    }

    public String toString() {
        return name.toLowerCase();
    }

    public long getDelay() {
        return delay;
    }

    public BufferedImage[] getImages() {
        return images;
    }

}
