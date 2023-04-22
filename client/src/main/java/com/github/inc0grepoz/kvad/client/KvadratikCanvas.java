package com.github.inc0grepoz.kvad.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMenu;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMenuTitle;
import com.github.inc0grepoz.kvad.utils.FrapsCounter;
import com.github.inc0grepoz.kvad.worker.RenderWorker;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    public boolean drawColliders, miscInfo;

    private final @Getter KvadratikGame game;
    private final @Getter RenderWorker worker;
    private final FrapsCounter fps = new FrapsCounter();

    private @Getter @Setter CanvasMenu menu;

    public KvadratikCanvas(KvadratikGame game, int x, int y) {
        this.game = game;
        worker = new RenderWorker(this);
    }

    public void setFrapsPerSecond(int fraps) {
        worker.setDelay(1000L / fraps);
    }

    @Override
    public void paint(Graphics g) {
        int cWidth = getWidth(), cHeight = getHeight();
        BufferedImage image = new BufferedImage(cWidth, cHeight, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = image.createGraphics();

        Session session = game.getSession();
        if (session == null) {
            if (menu != null) {
                menu.render(g2d);
            }
        } else {
            session.getPhysics().tick();

            Level level = session.getLevel();
            Camera cam = level.getCamera();

            // Drawing all entities
            cam.scale(this);
            g2d.setColor(Color.BLACK);

            int renEnts = level.renEntsStreamSorted()
                    .map(o -> o.render(g2d, cam) ? 1 : 0)
                    .reduce(0, Integer::sum);

            // Showing misc info
            if (miscInfo) {
                g2d.drawString("FPS: " + fps.getFPS(), 10, 10);
                g2d.drawString("Res: " + getWidth() + "x" + getHeight(), 10, 25);
                g2d.drawString("Ren-ents: " + renEnts, 10, 40);
            }
        }

        game.getClient().getChat().render(g2d);

        g2d.dispose();
        g.drawImage(image, 0, 0, cWidth, cHeight, this);
    }

    public void mainMenu() {
        menu = new CanvasMenuTitle(this, getGraphics());
    }

}
