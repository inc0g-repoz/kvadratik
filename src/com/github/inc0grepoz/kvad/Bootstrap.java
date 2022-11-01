package com.github.inc0grepoz.kvad;

public class Bootstrap {

    public static void main(String[] args) {
        KvadratikGame game = new KvadratikGame();
        game.setSize(640, 480);
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

}
