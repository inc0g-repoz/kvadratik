package com.github.inc0grepoz.kvad.common.utils;

public class TimeGap {

    private static long gap;

    public static long get() {
        long then = gap;
        gap = System.currentTimeMillis();
        return gap - then;
    }

}
