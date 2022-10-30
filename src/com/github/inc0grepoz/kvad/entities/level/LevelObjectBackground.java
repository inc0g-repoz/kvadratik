package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.GameFrame;
import com.github.inc0grepoz.kvad.entities.Camera;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(int[] rect, Level level, LevelObjectAnim type) {
        super(rect, level, type);
        this.color = null;
    }

    public LevelObjectBackground(int[] rect, Level level, Color color) {
        super(rect, level, LevelObjectAnim.COLOR);
        this.color = color;
    }

    @Override
    public boolean render(Graphics graphics, Camera camera) {
        GameFrame gf = getLevel().getGame();
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
