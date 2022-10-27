package com.github.inc0grepoz.kvad.entities;

public enum BeingType {

    PLAYER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
