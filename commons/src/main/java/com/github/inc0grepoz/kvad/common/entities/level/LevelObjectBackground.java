package com.github.inc0grepoz.kvad.common.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.common.Kvadratik;
import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.Camera;
import com.github.inc0grepoz.kvad.common.utils.Platform;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            LevelObjectTemplate temp, LevelObjectAnim anim) {
        super(level, rect, temp, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            LevelObjectTemplate temp, Color color) {
        super(level, rect, temp, LevelObjectAnim.COLOR);
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
