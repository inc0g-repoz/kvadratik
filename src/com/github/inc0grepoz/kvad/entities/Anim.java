package com.github.inc0grepoz.kvad.entities;

public class Anim {

    public static enum AnimType {
        HIT, IDLE, RUN
    }

    private AnimType type = AnimType.IDLE;
    private long expiry; // 0 for infinite duration

    public void apply(LivingEntity entity, AnimType type, int duration) {
        this.type = type;
        expiry = System.currentTimeMillis() + duration;
    }

    public AnimType getType() {
        return type;
    }

    public boolean hasExpired() {
        return expiry > 0 && System.currentTimeMillis() > expiry;
    }

}
