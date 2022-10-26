package com.github.inc0grepoz.kvad.utils;

public class FrapsCounter {

    private boolean enable;
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

    public boolean isEnabled() {
        return enable;
    }

    public void setEnabled(boolean enable) {
        this.enable = enable;
    }

}
