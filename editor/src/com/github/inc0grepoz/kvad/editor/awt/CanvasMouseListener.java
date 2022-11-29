package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class CanvasMouseListener implements MouseListener {

    private final CanvasRenderer canvas;

    public CanvasMouseListener(CanvasRenderer canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        KvadratikEditor editor = canvas.getEditor();
        Level level = editor.getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point loc = event.getPoint();
        loc.x += cam.x;
        loc.y += cam.y;

        Renderable renEnt = level.renEntsStreamReversed()
                .filter(e -> e.getRectangle().contains(loc))
                .findFirst().orElse(null);
        if (renEnt == null) {
            return;
        }

        Selection sel = editor.getSelection();
        if (renEnt.selected) {
            sel.clearSelection();
        } else {
            sel.setTarget(renEnt);
        }
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
