package com.github.inc0grepoz.kvad.editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;
import com.github.inc0grepoz.kvad.worker.RenderWorker;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    public boolean miscInfo = true;

    private final KvadratikEditor editor;
    private final RenderWorker worker;
    private final FrapsCounter fps = new FrapsCounter();

    public KvadratikCanvas(KvadratikEditor game, int x, int y) {
        this.editor = game;
        worker = new RenderWorker(this);
    }

    public RenderWorker getWorker() {
        return worker;
    }

    public void setFrapsPerSecond(int fraps) {
        worker.setDelay(1000L / fraps);
    }

    @Override
    public void paint(Graphics g) {
        int gw = getBounds().width, gh = getBounds().width;
        BufferedImage image = new BufferedImage(gw, gh, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = image.createGraphics();

        Level level = editor.getLevel();
        if (level == null) {

            // TODO: Some menu code probably

        } else {
            Camera cam = level.getCamera();

            // Drawing all entities
            cam.scale(editor);
            g2d.setColor(Color.BLACK);

            int renEnts = level.entitiesStream()
                    .map(o -> o.render(g2d, cam) ? 1 : 0)
                    .reduce(0, Integer::sum);

            // Showing misc info
            if (miscInfo) {
                g2d.drawString("FPS: " + fps.getFPS(), 10, 10);
                g2d.drawString("Ren-ents: " + renEnts, 10, 25);
            }
        }

        g2d.dispose();
        g.drawImage(image, 0, 0, gw, gh, this);
    }

}
