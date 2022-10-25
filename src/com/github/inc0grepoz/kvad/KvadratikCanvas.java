package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    private final KvadratikGame game;
    private final RenderWorker worker;

    public KvadratikCanvas(KvadratikGame game, int x, int y) {
        this.game = game;
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

        // Cleaning stuff
        g.clearRect(0, 0, getWidth(), getHeight());

        Level level = game.getLevel();
        Camera cam = level.getCamera();
        Player player = level.getPlayer();

        // Focusing the camera on the player
        if (cam.getMode() == CameraMode.FOLLOW) {
            cam.focus(player);
        }

        // Drawing all entities
        level.getLevelObjects().forEach(o -> o.render(g, cam));
        level.getBeings().forEach(e -> e.render(g, cam));
    }

}
