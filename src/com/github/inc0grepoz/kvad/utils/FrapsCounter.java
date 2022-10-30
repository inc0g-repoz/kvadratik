package com.github.inc0grepoz.kvad.utils;

public class FrapsCounter {

    private long last;
    private int fraps, counter;

    public int getFPS() {
        long now = System.currentTimeMillis();
        if (last == 0L || now - last > 1000L) {
            last = now;
            fraps = counter;
            counter = 0;
        } else {
            counter++;
        }
        return fraps;
    }

}
