package com.github.inc0grepoz;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.utils.XmlParser;

public class Test {

    public static void main(String[] args) {
        String path = "assets/levels/whitespace.xml";
        long now = System.currentTimeMillis();
        XmlParser.readFromString(KvadratikGame.getAssets().textFile(path))
                .forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println();
        System.out.println("Read in " + (System.currentTimeMillis() - now) + " millis");
    }

}
