package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, Color color) {
        super(level, rect, collSize, collOffset, LevelObjectAnim.COLOR);
        this.color = color;
    }

    @Override
    public int getRenderPriority() {
        int winHeight = KvadratikGame.INSTANCE.getCanvas().getHeight();
        return super.getRenderPriority() - winHeight;
    }

    @Override
    public boolean render(Graphics graphics, Camera camera) {
        KvadratikGame gf = KvadratikGame.INSTANCE;
        int gfw = gf.getWidth(), gfh = gf.getHeight();
        if (getAnim() == LevelObjectAnim.COLOR) {
            Color color = graphics.getColor();
            graphics.setColor(this.color);
            graphics.fillRect(0, 0, gfw, gfw);
            graphics.setColor(color);
        } else if (color != null) {
            draw(graphics, 0, 0, gfw, gfh);
        }
        return true;
    }

}
