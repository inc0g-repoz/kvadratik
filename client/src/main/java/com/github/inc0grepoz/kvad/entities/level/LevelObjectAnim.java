package com.github.inc0grepoz.kvad.entities.level;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.github.inc0grepoz.kvad.client.KvadratikGame;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LevelObjectAnim {

    public static final LevelObjectAnim COLOR = new LevelObjectAnim(0, "color");

    private static Map<String, LevelObjectAnim> anims;

    public static void init(Map<String, LevelObjectAnim> anims) {
        if (LevelObjectAnim.anims != null) {
            throw new IllegalStateException("Being types already have been initialized");
        }
        LevelObjectAnim.anims = anims;
    }

    public static LevelObjectAnim valueOf(String name) {
        return anims.get(name);
    }

    private String name;
    private String[] paths;
    private long delay;

    // Cache
    private BufferedImage[] images;

    public LevelObjectAnim(long delay, String name) {
        this.name = name;
        this.delay = delay;
    }

    @Override
    public String toString() {
        return name.toLowerCase();
    }

    public void precacheSprites() {
        if (LevelObjectAnim.anims != null) {
            throw new IllegalStateException("Sprites already have been precached");
        }

        images = new BufferedImage[paths.length];

        for (int i = 0; i < images.length; i++) {
            String btPath = "assets/objects/sprites/" + this + "/" + paths[i];
            images[i] = KvadratikGame.ASSETS.image(btPath);
        }
    }

}
