package com.github.inc0grepoz.kvad.common.utils;

import java.awt.Color;
import java.util.Random;

public class RGB {

    private static final Random RANDOM = new Random();

    public static Color get(int... rgb) {
        float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public static Color random() {
        return new Color(RANDOM.nextFloat(), RANDOM.nextFloat(), RANDOM.nextFloat());
    }

}
