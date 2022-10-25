package com.github.inc0grepoz.kvad.entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Anim {

    PLAYER_HIT_W(0),
    PLAYER_HIT_A(0),
    PLAYER_HIT_S(0),
    PLAYER_HIT_D(0),
    PLAYER_IDLE_W(0,
            "src/assets/snuny/idle_w.png"),
    PLAYER_IDLE_A(0,
            "src/assets/snuny/idle_a.png"),
    PLAYER_IDLE_S(0,
            "src/assets/snuny/idle_s.png"),
    PLAYER_IDLE_D(0,
            "src/assets/snuny/idle_d.png"),
    PLAYER_RUN_W(0),
    PLAYER_RUN_A(0),
    PLAYER_RUN_S(0),
    PLAYER_RUN_D(0),
    PLAYER_WALK_W(200,
            "src/assets/snuny/walk_w_1.png",
            "src/assets/snuny/idle_w.png",
            "src/assets/snuny/walk_w_2.png",
            "src/assets/snuny/idle_w.png"),
    PLAYER_WALK_A(200,
            "src/assets/snuny/walk_a_1.png",
            "src/assets/snuny/idle_a.png",
            "src/assets/snuny/walk_a_2.png",
            "src/assets/snuny/idle_a.png"),
    PLAYER_WALK_S(200,
            "src/assets/snuny/walk_s_1.png",
            "src/assets/snuny/idle_s.png",
            "src/assets/snuny/walk_s_2.png",
            "src/assets/snuny/idle_s.png"),
    PLAYER_WALK_D(200,
            "src/assets/snuny/walk_d_1.png",
            "src/assets/snuny/idle_d.png",
            "src/assets/snuny/walk_d_2.png",
            "src/assets/snuny/idle_d.png");

    private final long delay;
    private final BufferedImage[] images;

    Anim(long delay, String... paths) {
        this.delay = delay;
        images = new BufferedImage[paths.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = readImage(paths[i]);
        }
    }

    public long getDelay() {
        return delay;
    }

    public BufferedImage[] getImages() {
        return images;
    }

    private BufferedImage readImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Invalid image path: " + path);
            System.exit(0);
        }
        return null;
    }

}
