package com.github.inc0grepoz.kvad.entities;

public enum BeingType {

    PLAYER(Anim.PLAYER_IDLE_S);

    private final Anim idle;

    BeingType(Anim idle) {
        this.idle = idle;
    }

    public Anim getIdleAnim() {
        return idle;
    }

}
