package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;

public class CanvasMouseListener implements MouseListener {

    private final KvadratikCanvas canvas;

    public CanvasMouseListener(KvadratikCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        CanvasMenu menu = canvas.getMenu();
        if (menu != null) {
            menu.click(event.getX(), event.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

}
