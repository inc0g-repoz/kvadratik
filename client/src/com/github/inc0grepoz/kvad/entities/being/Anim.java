package com.github.inc0grepoz.kvad.entities.being;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.github.inc0grepoz.kvad.KvadratikGame;

public enum Anim {

    HIT_W(0, Way.W),
    HIT_A(0, Way.A),
    HIT_S(0, Way.S),
    HIT_D(0, Way.D),
    IDLE_W(0, Way.W,
            "idle_w.png"),
    IDLE_A(0, Way.A,
            "idle_a.png"),
    IDLE_S(0, Way.S,
            "idle_s.png"),
    IDLE_D(0, Way.D,
            "idle_d.png"),
    RUN_W(100, Way.W,
            "run_w_1.png",
            "run_w_2.png",
            "idle_w.png",
            "run_w_3.png",
            "run_w_4.png",
            "idle_w.png"),
    RUN_A(100, Way.A,
            "run_a_1.png",
            "run_a_2.png",
            "idle_a.png",
            "run_a_3.png",
            "run_a_4.png",
            "idle_a.png"),
    RUN_S(100, Way.S,
            "run_s_1.png",
            "run_s_2.png",
            "idle_s.png",
            "run_s_3.png",
            "run_s_4.png",
            "idle_s.png"),
    RUN_D(100, Way.D,
            "run_d_1.png",
            "run_d_2.png",
            "idle_d.png",
            "run_d_3.png",
            "run_d_4.png",
            "idle_d.png"),
    WALK_W(200, Way.W,
            "walk_w_1.png",
            "idle_w.png",
            "walk_w_2.png",
            "idle_w.png"),
    WALK_A(200, Way.A,
            "walk_a_1.png",
            "idle_a.png",
            "walk_a_2.png",
            "idle_a.png"),
    WALK_S(200, Way.S,
            "walk_s_1.png",
            "idle_s.png",
            "walk_s_2.png",
            "idle_s.png"),
    WALK_D(200, Way.D,
            "walk_d_1.png",
            "idle_d.png",
            "walk_d_2.png",
            "idle_d.png");

    public static enum Way {

        W(0, -1), A(-1, 0), S(0, 1), D(1, 0);

        public final int x, y;

        Way(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public final Way way;
    public final long delay;

    private final HashMap<BeingType, BufferedImage[]> images = new HashMap<>();

    Anim(long delay, Way way, String... paths) {
        this.delay = delay;
        this.way = way;

        BeingType[] bt = BeingType.values();
        for (int i = 0; i < bt.length; i++) {
            BufferedImage[] btImages = new BufferedImage[paths.length];
            for (int j = 0; j < btImages.length; j++) {
                String btPath = "assets/sprites/" + bt[i] + "/" + paths[j];
                btImages[j] = KvadratikGame.getAssets().image(btPath);
            }
            images.put(bt[i], btImages);
        }
    }

    public BufferedImage[] getImages(BeingType beintType) {
        return images.getOrDefault(beintType, null);
    }

}
