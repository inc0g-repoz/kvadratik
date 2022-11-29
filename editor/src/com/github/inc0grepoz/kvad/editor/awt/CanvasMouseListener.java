package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Logger;

public class CanvasMouseListener implements MouseListener {

    private final CanvasRenderer canvas;

    public CanvasMouseListener(CanvasRenderer canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        Level level = canvas.getEditor().getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point loc = event.getPoint();
        loc.x += cam.x;
        loc.y += cam.y;

        Renderable renEnt = level.renEntsStream()
                .filter(e -> e.getRectangle().contains(loc))
                .findFirst().orElse(null);
        if (renEnt == null) {
            return;
        }

        Logger.info(renEnt.getClass().getSimpleName());
        Logger.info(loc.x + ", " + loc.y);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

}
