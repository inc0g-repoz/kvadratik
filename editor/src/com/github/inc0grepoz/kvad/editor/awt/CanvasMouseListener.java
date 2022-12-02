package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Point;
import java.awt.Rectangle;
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
        Selection sel = editor.getSelection();
        Level level = editor.getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point loc = event.getPoint();
        loc.x += cam.x;
        loc.y += cam.y;

        switch (sel.getMode()) {
            case POINT: {
                Renderable renEnt = level.renEntsStreamReversed()
                        .filter(e -> e.getRectangle().contains(loc))
                        .findFirst().orElse(null);
                if (renEnt == null) {
                    return;
                }

                if (renEnt.selected) {
                    sel.selTar.clearSelection();
                } else {
                    sel.selTar.setTarget(renEnt);
                }
                break;
            }
            case GRID: {
                String strValue = editor.getPanel().getObjectsList().getSelectedValue();
                if (strValue == null) {
                    break;
                }

                Rectangle selRect = sel.selGrid.rect;
                level.renEntsStreamReversed()
                        .filter(e -> e.getRectangle().intersects(selRect))
                        .forEach(Renderable::delete);

                KvadratikEditor.OBJECT_FACTORY.create(strValue, level, selRect.getLocation());
                break;
            }
            default:
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
