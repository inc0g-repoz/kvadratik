package com.github.inc0grepoz.kvad.entities.level;

import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.KvadratikGame;

public enum LevelObjectAnim {

    LAPTOP(250,
            "state_1.png",
            "state_2.png"),
    MEWO(1000,
            "state_1.png",
            "state_2.png");

    private final long delay;
    private final BufferedImage[] images;

    LevelObjectAnim(long delay, String... paths) {
        this.delay = delay;
        images = new BufferedImage[paths.length];

        for (int i = 0; i < images.length; i++) {
            String btPath = "assets/sprites/" + this + "/" + paths[i];
            System.out.println("Loading " + btPath);
            images[i] = KvadratikGame.getAssets().image(btPath);
        }
    }

    public String toString() {
        return name().toLowerCase();
    }

    public long getDelay() {
        return delay;
    }

    public BufferedImage[] getImages() {
        return images;
    }

}
