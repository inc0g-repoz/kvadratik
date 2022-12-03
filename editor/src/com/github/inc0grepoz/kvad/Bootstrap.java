package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

public class Bootstrap {

    private static final KvadratikEditor GAME = new KvadratikEditor();

    static {
        GAME.setSize(900, 550);
        GAME.setResizable(true);
        GAME.setLocationRelativeTo(null);
        GAME.setVisible(true);
    }

    public static void main(String[] args) {
        GAME.run();
    }

}
