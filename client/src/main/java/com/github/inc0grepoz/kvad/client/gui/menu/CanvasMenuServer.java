package com.github.inc0grepoz.kvad.client.gui.menu;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.client.KvadratikGame;

import lombok.Getter;

public class CanvasMenuServer extends CanvasMenu {

    private final @Getter String title = "Multiplayer";
    private CanvasItemTextField citfServerIp;

    public CanvasMenuServer(KvadratikCanvas canvas, Graphics buff) {
        citfServerIp = addTextField("Enter the server IP:", "127.0.0.1");
        addButton("Connect", () -> {
            KvadratikGame.INSTANCE.joinServer(citfServerIp.getContent());
            canvas.setMenu(null);
        });
        alignItems(canvas, buff);
    }

}
