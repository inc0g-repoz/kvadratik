package com.github.inc0grepoz.kvad.entities.being;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.client.KvadratikGame;

public enum Anim {

    HIT_W(0, 0, Way.W),
    HIT_A(0, 0, Way.A),
    HIT_S(0, 0, Way.S),
    HIT_D(0, 0, Way.D),
    IDLE_W(0, 0, Way.W,
            "idle_w.png"),
    IDLE_A(0, 0, Way.A,
            "idle_a.png"),
    IDLE_S(0, 0, Way.S,
            "idle_s.png"),
    IDLE_D(0, 0, Way.D,
            "idle_d.png"),
    RUN_W(100, 12, Way.W,
            "run_w_1.png",
            "run_w_2.png",
            "idle_w.png",
            "run_w_3.png",
            "run_w_4.png",
            "run_w_3.png",
            "idle_w.png",
            "run_w_2.png"),
    RUN_A(100, 12, Way.A,
            "run_a_1.png",
            "run_a_2.png",
            "idle_a.png",
            "run_a_3.png",
            "run_a_4.png",
            "run_a_3.png",
            "idle_a.png",
            "run_a_2.png"),
    RUN_S(100, 12, Way.S,
            "run_s_1.png",
            "run_s_2.png",
            "idle_s.png",
            "run_s_3.png",
            "run_s_4.png",
            "run_s_3.png",
            "idle_s.png",
            "run_s_2.png"),
    RUN_D(100, 12, Way.D,
            "run_d_1.png",
            "run_d_2.png",
            "idle_d.png",
            "run_d_3.png",
            "run_d_4.png",
            "run_d_3.png",
            "idle_d.png",
            "run_d_2.png"),
    WALK_W(200, 4, Way.W,
            "walk_w_1.png",
            "idle_w.png",
            "walk_w_2.png",
            "idle_w.png"),
    WALK_A(200, 4, Way.A,
            "walk_a_1.png",
            "idle_a.png",
            "walk_a_2.png",
            "idle_a.png"),
    WALK_S(200, 4, Way.S,
            "walk_s_1.png",
            "idle_s.png",
            "walk_s_2.png",
            "idle_s.png"),
    WALK_D(200, 4, Way.D,
            "walk_d_1.png",
            "idle_d.png",
            "walk_d_2.png",
            "idle_d.png");

    public static enum Way {

        W(0, -1), A(-1, 0), S(0, 1), D(1, 0);

        public final int x, y;

        public static Way fromVector(Vector vector) {
            Way[] ways = values();
            for (int i = 0; i < ways.length; i++) {
                if (compareDigits(ways[i].x, vector.x)
                        && compareDigits(ways[i].y, vector.y)) {
                    return ways[i];
                }
            }
            return null;
        }

        private static boolean compareDigits(double a, double b) {
            return a > 0 && b > 0
                    || a == 0 && b == 0
                    || a < 0 && b < 0;
        }

        Way(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public final Way way;
    public final long delay;
    public final int moveSpeed;

    private final HashMap<String, BufferedImage[]> images = new HashMap<>();

    Anim(long delay, int speed, Way way, String... paths) {
        this.delay = delay;
        this.moveSpeed = speed;
        this.way = way;

        String[] bt = KvadratikGame.BEING_FACTORY.getTypes();
        for (int i = 0; i < bt.length; i++) {
            BufferedImage[] btImages = new BufferedImage[paths.length];
            for (int j = 0; j < btImages.length; j++) {
                String btPath = "assets/beings/sprites/" + bt[i] + "/" + paths[j];
                btImages[j] = KvadratikGame.ASSETS.image(btPath);
            }
            images.put(bt[i], btImages);
        }
    }

    public BufferedImage[] getImages(String beingType) {
        return images.getOrDefault(beingType, null);
    }

}
