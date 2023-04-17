package com.github.inc0grepoz.kvad.client.awt;

import java.awt.Canvas;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.client.KvadratikGame;

import lombok.Getter;

public class CanvasMenuTitle extends CanvasMenu {

    private final @Getter String title = "kvadratik";

    public CanvasMenuTitle(Canvas canvas, Graphics buff) {
        addButton("Singleplayer", () -> {
            KvadratikGame.INSTANCE.run();
        });
        addButton("Multiplayer", () -> {
            
        });
        alignItems(canvas, buff);
    }

}
