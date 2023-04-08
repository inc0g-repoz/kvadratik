package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.utils.Vector;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, LevelObjectAnim anim,
            String type) {
        super(level, rect, collSize, collOffset, anim, type);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector collOffset, Color color,
            String type) {
        super(level, rect, collSize, collOffset, LevelObjectAnim.COLOR, type);
        this.color = color;
    }

    @Override
    public int getRenderPriority() {
        int winHeight = getLevel().getEditor().getCanvas().getHeight();
        return super.getRenderPriority() - winHeight;
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
