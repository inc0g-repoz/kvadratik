package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.client.KvadratikGame;

import lombok.Getter;

public class CanvasMenuTitle extends CanvasMenu {

    private final @Getter String title = "kvadratik";

    public CanvasMenuTitle(KvadratikCanvas canvas, Graphics buff) {
        addButton("Singleplayer", () -> {
            KvadratikGame.INSTANCE.run();
            canvas.setMenu(null);
        });
        addButton("Multiplayer", () -> {
            
        });
        alignItems(canvas, buff);
    }

}
