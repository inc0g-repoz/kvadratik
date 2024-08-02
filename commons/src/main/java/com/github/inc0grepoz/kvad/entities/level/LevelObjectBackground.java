package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.utils.Platform;
import com.github.inc0grepoz.kvad.utils.Vector2D;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, LevelObjectAnim anim) {
        super(level, rect, collSize, collOffset, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, Color color) {
        super(level, rect, collSize, collOffset, LevelObjectAnim.COLOR);
        this.color = color;
    }

    @Override
    public int getRenderPriority() {
        int winHeight = Platform.getInstance().getCanvas().getHeight();
        return (int) (getLevel().getCamera().getRectangle().y) - winHeight;
    }

    @Override
    public boolean render(Graphics gfx, Camera camera) {
        Kvadratik gf = Platform.getInstance();
        int gfw = gf.getWidth(), gfh = gf.getHeight();
        if (getAnim() == LevelObjectAnim.COLOR) {
            Color color = gfx.getColor();
            gfx.setColor(this.color);
            gfx.fillRect(0, 0, gfw, gfw);
            gfx.setColor(color);
        } else if (color != null) {
            draw(gfx, 0, 0, gfw, gfh);
        }
        return true;
    }

}
