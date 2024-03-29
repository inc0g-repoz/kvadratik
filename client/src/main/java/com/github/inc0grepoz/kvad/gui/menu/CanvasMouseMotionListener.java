package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.github.inc0grepoz.kvad.client.KvadratikCanvas;

public class CanvasMouseMotionListener implements MouseMotionListener {

    private final KvadratikCanvas canvas;

    public CanvasMouseMotionListener(KvadratikCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CanvasMenu menu = canvas.getMenu();
        if (menu != null) {
            menu.hover(e.getX(), e.getY());
        }
    }

}
