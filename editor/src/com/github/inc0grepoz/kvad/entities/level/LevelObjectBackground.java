package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
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
    public boolean render(Graphics graphics, Camera camera) {
        KvadratikEditor gf = getLevel().getEditor();
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
