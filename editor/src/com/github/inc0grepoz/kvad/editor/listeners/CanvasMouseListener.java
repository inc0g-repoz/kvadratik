package com.github.inc0grepoz.kvad.editor.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.inc0grepoz.kvad.editor.KvadratikCanvas;
import com.github.inc0grepoz.kvad.utils.Logger;

public class CanvasMouseListener implements MouseListener {

    private final KvadratikCanvas canvas;

    public CanvasMouseListener(KvadratikCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        Logger.info(point.x + ", " + point.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}
