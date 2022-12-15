package com.github.inc0grepoz.kvad.entities.being;

public enum Anim {

    HIT_W  (0, Way.W),
    HIT_A  (0, Way.A),
    HIT_S  (0, Way.S),
    HIT_D  (0, Way.D),
    IDLE_W (0, Way.W),
    IDLE_A (0, Way.A),
    IDLE_S (0, Way.S),
    IDLE_D (0, Way.D),
    RUN_W  (12, Way.W),
    RUN_A  (12, Way.A),
    RUN_S  (12, Way.S),
    RUN_D  (12, Way.D),
    WALK_W (4, Way.W),
    WALK_A (4, Way.A),
    WALK_S (4, Way.S),
    WALK_D (4, Way.D);

    public static enum Way {

        W(0, -1), A(-1, 0), S(0, 1), D(1, 0);

        public final int x, y;

        Way(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public final int speed;
    public final Way way;

    Anim(int speed, Way way) {
        this.speed = speed;
        this.way = way;
    }

}
