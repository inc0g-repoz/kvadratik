package com.github.inc0grepoz.kvad.utils;

import java.awt.Color;

public class RGB {

    public static Color get(int[] rgb) {
        float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

}
