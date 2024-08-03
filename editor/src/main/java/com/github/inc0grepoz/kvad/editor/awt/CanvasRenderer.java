package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection;
import com.github.inc0grepoz.kvad.editor.Selection.SelectionMode;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;
import com.github.inc0grepoz.kvad.worker.RenderWorker;

import lombok.Getter;

@SuppressWarnings("serial")
public class CanvasRenderer extends Canvas {

    public boolean miscInfo = true;

    private final KvadratikEditor editor;
    private final @Getter RenderWorker worker;
    private final FrapsCounter fps = new FrapsCounter();

    public CanvasRenderer(KvadratikEditor editor, int x, int y) {
        this.editor = editor;
        worker = new RenderWorker(this);
    }

    public void setFrapsPerSecond(int fraps) {
        worker.setDelay(1000L / fraps);
    }

    @Override
    public void paint(Graphics g) {
        int gw = getWidth(), gh = getHeight();
        BufferedImage image = new BufferedImage(gw, gh, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = image.createGraphics();

        Level level = editor.getLevel();
        if (level == null) {

            // TODO: Some menu code probably

        } else {
            Camera cam = level.getCamera();

            // Drawing all entities
            cam.scale(this);
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
                g2d.drawString("FPS: " + fps.getFPS(), 10, 10);
                g2d.drawString("Ren-ents: " + renEnts, 10, 25);
            }
        }

        g2d.dispose();
        g.drawImage(image, 0, 0, gw, gh, this);
    }

    public KvadratikEditor getEditor() {
        return editor;
    }

}
