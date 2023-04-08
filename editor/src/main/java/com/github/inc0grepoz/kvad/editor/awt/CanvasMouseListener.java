package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.factory.RenderableFactory;
import com.github.inc0grepoz.kvad.entities.factory.RenderableTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class CanvasMouseListener implements MouseListener {

    private final CanvasRenderer canvas;
    private final PopupRenent popupRenent = new PopupRenent();

    public CanvasMouseListener(CanvasRenderer canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        KvadratikEditor editor = canvas.getEditor();
        Selection sel = editor.getSelection();
        Level level = editor.getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point loc = Point.fromAwtPoint(event.getPoint());
        loc.x += cam.x;
        loc.y += cam.y;

        switch (sel.getMode()) {
            case POINT: {
                // LMB
                if (event.getButton() == 3) {
                    popupRenent.renEnt = level.renEntsStreamReversed()
                            .filter(e -> e.getRectangle().contains(loc))
                            .findFirst().orElse(null);
                    popupRenent.show(canvas, event.getPoint().x, event.getPoint().y);
                }

                // MMB
                else if (event.getButton() == 2) {
                    
                }

                // RMB    
                else if (sel.selTar.getTarget() != null) {
                    sel.selTar.clearSelection();
                } else {
                    Renderable renEnt = level.renEntsStreamReversed()
                            .filter(e -> e.getRectangle().contains(loc))
                            .findFirst().orElse(null);
                    if (renEnt != null) {
                        sel.selTar.setTarget(renEnt);
                    }
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

                RenderableFactory factory = sel.selGrid.getFactory();
                RenderableTemplate temp = factory.getTemplate(strValue);

                if (editor.getPanel().isAutoGridSizeEnabled()) {
                    Dimension rSize = temp.getSize();
                    selRect.width = rSize.width;
                    selRect.height = rSize.height;
                }

                factory.create(strValue, level, selRect.getLocation());
                break;
            }
            default:
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
