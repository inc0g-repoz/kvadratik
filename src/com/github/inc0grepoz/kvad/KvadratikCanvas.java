package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    private final KvadratikGame game;
    private final RenderWorker worker;
    private final FrapsCounter fps = new FrapsCounter();

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

    @Override
    public void paint(Graphics g) {
        BufferedImage image = new BufferedImage(game.getWidth(), game.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Clearing the buffer
        g2d.fillRect(0, 0, game.getWidth(), game.getHeight());

        Level level = game.getLevel();
        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        Camera cam = level.getCamera();
        Player player = level.getPlayer();

        // Focusing the camera on the player
        if (cam.getMode() == CameraMode.FOLLOW) {
            cam.focus(player);
        }

        // Drawing all entities
        g2d.setColor(Color.BLACK);
        level.getLevelObjects().forEach(o -> o.render(g2d, cam));
        level.getBeings().forEach(e -> e.render(g2d, cam));

        // Showing misc info
        if (fps.isEnabled()) {
            g2d.drawString(String.valueOf(fps.getFPS()), 10, 10);
        }

        g2d.dispose();
        g.drawImage(image, 0, 0, game.getWidth(), game.getHeight(), this);
    }

}
