package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.inc0grepoz.kvad.common.awt.Canvas;
import com.github.inc0grepoz.kvad.common.entities.Camera;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection;
import com.github.inc0grepoz.kvad.editor.Selection.SelectionMode;

import lombok.Getter;

@SuppressWarnings("serial")
public class CanvasRenderer extends Canvas {

    public boolean miscInfo = true;

    private final @Getter KvadratikEditor editor;

    public CanvasRenderer(KvadratikEditor editor, int x, int y) {
        this.editor = editor;
    }

    @Override
    public void update(Graphics2D g2d) {
        Level level = editor.getLevel();
        if (level == null) {

            // TODO: Some menu code probably

        } else {
            Camera cam = level.getCamera();

            // Drawing all entities
            cam.scale();
            g2d.setColor(Color.BLACK);

            int renEnts = level.renEntsStreamSorted()
                    .map(o -> o.render(g2d, cam) ? 1 : 0)
                    .reduce(0, Integer::sum);

            Selection sel = editor.getSelection();
            SelectionMode selMode = sel.getMode();
            if (selMode == SelectionMode.GRID) {
                sel.selGrid.render(g2d, cam);
            }

            // Showing misc info
            if (miscInfo) {
                g2d.drawString("FPS: " + getFrapsPerSecond(), 10, 10);
                g2d.drawString("Ren-ents: " + renEnts, 10, 25);
            }
        }
    }

}
