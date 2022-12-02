package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.Renderable;

public class CanvasMouseMotionListener implements MouseMotionListener {

    private final CanvasRenderer canvas;

    public CanvasMouseMotionListener(CanvasRenderer canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        KvadratikEditor editor = canvas.getEditor();
        Point cam = editor.getLevel().getCamera().getRectangle().getLocation();
        switch (editor.selection.getMode()) {
            case POINT: {
                Renderable target = editor.selection.selTar.getTarget();
                if (target != null) {
                    target.teleport(cam.x + e.getX(), cam.y + e.getY());
                }
                break;
            }
            case GRID: {
                editor.selection.selGrid.locate(cam.x + e.getX(), cam.y + e.getY());
                break;
            }
            default:
        }
    }

}
