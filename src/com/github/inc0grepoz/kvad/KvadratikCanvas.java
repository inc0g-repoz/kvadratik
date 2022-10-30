package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    private final KvadratikGame game;
    private final RenderWorker worker;
    private final FrapsCounter fps = new FrapsCounter();

    private boolean drawColliders, miscInfo;

    public KvadratikCanvas(KvadratikGame game, int x, int y) {
        this.game = game;
        worker = new RenderWorker(this);
    }

    public RenderWorker getWorker() {
        return worker;
    }

    public FrapsCounter getFrapsCounter() {
        return fps;
    }

    public void setFrapsPerSecond(int fraps) {
        worker.setDelay(1000L / fraps);
    }

    public boolean isMiscInfoViewed() {
        return miscInfo;
    }

    public void setViewMiscInfo(boolean miscInfo) {
        this.miscInfo = miscInfo;
    }

    public boolean isDrawCollidersEnabled() {
        return drawColliders;
    }

    public void setDrawColliders(boolean drawColliders) {
        this.drawColliders = drawColliders;
    }

    @Override
    public void paint(Graphics g) {
        int gw = game.getWidth(), gh = game.getHeight();
        BufferedImage image = new BufferedImage(gw, gh, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = image.createGraphics();

        Level level = game.getLevel();
        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        Camera cam = level.getCamera();

        // Drawing all entities
        cam.scale(game);
        g2d.setColor(Color.BLACK);

        int renEnts = level.entitiesStream().map(o -> o.render(g2d, cam) ? 1 : 0)
                .reduce(0, Integer::sum);

        // Showing misc info
        if (miscInfo) {
            g2d.drawString("FPS: " + fps.getFPS(), 10, 10);
            g2d.drawString("Ren-ents: " + renEnts, 10, 25);
        }

        g2d.dispose();
        g.drawImage(image, 0, 0, game.getWidth(), game.getHeight(), this);
    }

}
