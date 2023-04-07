package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

public class Bootstrap {

    static {
        KvadratikEditor.INSTANCE.setTitle("kvadratik editor");
        KvadratikEditor.INSTANCE.applyIcon("assets/icon.png");
        KvadratikEditor.INSTANCE.setSize(900, 550);
        KvadratikEditor.INSTANCE.setResizable(true);
        KvadratikEditor.INSTANCE.setLocationRelativeTo(null);
        KvadratikEditor.INSTANCE.setVisible(true);
    }

    public static void main(String[] args) {
        KvadratikEditor.INSTANCE.run();
    }

}
