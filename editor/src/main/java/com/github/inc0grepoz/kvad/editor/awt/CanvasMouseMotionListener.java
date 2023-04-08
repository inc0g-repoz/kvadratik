package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Vector;

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
        Selection sel = editor.getSelection();
        Point cam = editor.getLevel().getCamera().getRectangle().getLocation();
        switch (sel.getMode()) {
            case POINT: {
                Renderable target = sel.selTar.getTarget();
                if (target != null) {
                    Rectangle ent = target.getRectangle();
                    target.teleport(cam.x + e.getX() - ent.width, cam.y + e.getY() - ent.height);
                }
                break;
            }
            case GRID: {
                EditorToolsPanel etp = editor.getPanel();
                if (etp.isAutoGridSizeEnabled()) {
                    // TODO: huh?
                } else {
                    Vector gs = etp.getGridSize();
                    sel.selGrid.setGridSize(gs.x, gs.y);
                }
                sel.selGrid.locate(cam.x + e.getX(), cam.y + e.getY());
                break;
            }
            default:
        }
    }

}
