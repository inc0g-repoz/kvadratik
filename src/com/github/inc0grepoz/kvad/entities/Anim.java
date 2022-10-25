package com.github.inc0grepoz.kvad.entities;

import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.KvadratikGame;

public enum Anim {

    PLAYER_HIT_W(0, Way.W),
    PLAYER_HIT_A(0, Way.A),
    PLAYER_HIT_S(0, Way.S),
    PLAYER_HIT_D(0, Way.D),
    PLAYER_IDLE_W(0, Way.W,
            "assets/snuny/idle_w.png"),
    PLAYER_IDLE_A(0, Way.A,
            "assets/snuny/idle_a.png"),
    PLAYER_IDLE_S(0, Way.S,
            "assets/snuny/idle_s.png"),
    PLAYER_IDLE_D(0, Way.D,
            "assets/snuny/idle_d.png"),
    PLAYER_RUN_W(0, Way.W),
    PLAYER_RUN_A(0, Way.A),
    PLAYER_RUN_S(0, Way.S),
    PLAYER_RUN_D(0, Way.D),
    PLAYER_WALK_W(400, Way.W,
            "assets/snuny/walk_w_1.png",
            "assets/snuny/idle_w.png",
            "assets/snuny/walk_w_2.png",
            "assets/snuny/idle_w.png"),
    PLAYER_WALK_A(400, Way.A,
            "assets/snuny/walk_a_1.png",
            "assets/snuny/idle_a.png",
            "assets/snuny/walk_a_2.png",
            "assets/snuny/idle_a.png"),
    PLAYER_WALK_S(400, Way.S,
            "assets/snuny/walk_s_1.png",
            "assets/snuny/idle_s.png",
            "assets/snuny/walk_s_2.png",
            "assets/snuny/idle_s.png"),
    PLAYER_WALK_D(400, Way.D,
            "assets/snuny/walk_d_1.png",
            "assets/snuny/idle_d.png",
            "assets/snuny/walk_d_2.png",
            "assets/snuny/idle_d.png");

    public static enum Way {
        W, A, S, D
    }

    private final long delay;
    private final Way way;
    private final BufferedImage[] images;

    Anim(long delay, Way way, String... paths) {
        this.delay = delay;
        this.way = way;
        images = new BufferedImage[paths.length];
        for (int i = 0; i < images.length; i++) {
            System.out.println("Loading " + paths[i]);
            images[i] = KvadratikGame.getAssets().image(paths[i]);
        }
    }

    public long getDelay() {
        return delay;
    }

    public Way getWay() {
        return way;
    }

    public BufferedImage[] getImages() {
        return images;
    }

}
