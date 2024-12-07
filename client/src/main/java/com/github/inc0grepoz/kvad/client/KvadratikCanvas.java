package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.inc0grepoz.kvad.awt.Canvas;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMenu;
import com.github.inc0grepoz.kvad.gui.menu.CanvasMenuTitle;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class KvadratikCanvas extends Canvas {

    public static String formatted(double amount) {
        return String.format("%,.3f", amount);
    }

    public boolean drawColliders, miscInfo;

    private final @Getter KvadratikGame game;

    private @Getter @Setter CanvasMenu menu;

    public KvadratikCanvas(KvadratikGame game, int x, int y) {
        this.game = game;
    }

    @Override
    protected void update(Graphics2D g2d) {
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
            cam.scale();
            g2d.setColor(Color.BLACK);

            int renEnts = level.renEntsStreamSorted()
                    .map(o -> o.render(g2d, cam) ? 1 : 0)
                    .reduce(0, Integer::sum);

            // Showing misc info
            if (miscInfo) {
                g2d.drawString("FPS: " + getFrapsPerSecond(), 10, 10);
                g2d.drawString("Res: " + getWidth() + "x" + getHeight(), 10, 25);
                g2d.drawString("Ren-ents: " + renEnts, 10, 40);

                Rectangle prect = level.getPlayer().getRectangle();
                g2d.drawString("Position: " + formatted(prect.x) + ", " + formatted(prect.y), 10, 55);
            }
        }

        game.getClient().getChat().render(g2d);
    }

    public void mainMenu() {
        menu = new CanvasMenuTitle(this, getGraphics());
    }

}
