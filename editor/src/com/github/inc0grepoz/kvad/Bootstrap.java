package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.editor.KvadratikGame;

public class Bootstrap {

    private static final KvadratikGame GAME = new KvadratikGame();

    static {
        GAME.setSize(640, 480);
        GAME.setResizable(false);
        GAME.setLocationRelativeTo(null);
        GAME.setVisible(true);
    }

    public static void main(String[] args) {
        GAME.run();
    }

}
