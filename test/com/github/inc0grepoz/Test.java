package com.github.inc0grepoz;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.utils.XML;

public class Test {

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        XML xml = KvadratikGame.getAssets().readXml("assets/levels/whitespace.xml");
        System.out.println();
        System.out.println("Read in " + (System.currentTimeMillis() - now) + " millis");
    }

}
