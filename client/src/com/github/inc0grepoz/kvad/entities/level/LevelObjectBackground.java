package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, int[] rect, LevelObjectAnim anim) {
        super(level, rect, anim);
        this.color = null;
    }

    public LevelObjectBackground(Level level, int[] rect, Color color) {
        super(level, rect, LevelObjectAnim.COLOR);
        this.color = color;
    }

    @Override
    public boolean render(Graphics graphics, Camera camera) {
        KvadratikGame gf = getLevel().getGame();
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
