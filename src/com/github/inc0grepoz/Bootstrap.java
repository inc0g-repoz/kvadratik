package com.github.inc0grepoz;

import com.github.inc0grepoz.kvad.KvadratikGame;

public class Bootstrap {

    public static void main(String[] args) {
        GameFrame game = new KvadratikGame();
        game.applyIcon("assets/icon.png");
        game.setTitle("kvadratik");
        game.setSize(640, 480);
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

}
