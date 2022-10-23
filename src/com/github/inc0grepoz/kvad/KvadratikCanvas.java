package com.github.inc0grepoz.kvad;

import java.awt.Canvas;
import java.awt.Graphics;

import com.github.inc0grepoz.Game;
import com.github.inc0grepoz.kvad.entities.Camera;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    private final Game game;
    private final RenderWorker worker;

    public KvadratikCanvas(KvadratikGame game, int x, int y) {
        this.game = game;
        worker = new RenderWorker(this, 33L);
    }

    public RenderWorker getWorker() {
        return worker;
    }

    @Override
    public void update(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paint(g);
    }

    public void update() {
        Graphics g = getGraphics();
        if (g == null) {
            System.out.println("Null graphics");
            return;
        }

        // Clearing stuff
        update(g);

        // Drawing the player
        Camera cam = game.getLevel().getCamera();
        game.getLevel().getPlayer().draw(g, cam);
    }

}
