package com.github.inc0grepoz.kvad.entities;

public enum Anim {

    HIT_W  (Way.W),
    HIT_A  (Way.A),
    HIT_S  (Way.S),
    HIT_D  (Way.D),
    IDLE_W (Way.W),
    IDLE_A (Way.A),
    IDLE_S (Way.S),
    IDLE_D (Way.D),
    RUN_W  (Way.W),
    RUN_A  (Way.A),
    RUN_S  (Way.S),
    RUN_D  (Way.D),
    WALK_W (Way.W),
    WALK_A (Way.A),
    WALK_S (Way.S),
    WALK_D (Way.D);

    public static enum Way {

        W(0, -1), A(-1, 0), S(0, 1), D(1, 0);

        public final int x, y;

        Way(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public final Way way;

    Anim(Way way) {
        this.way = way;
    }

}
