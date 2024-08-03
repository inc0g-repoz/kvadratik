package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.utils.Vector2D;

public class LevelObjectBackground extends LevelObjectAnimated {

    private final Color color;

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, LevelObjectAnim anim,
            String type) {
        super(level, rect, collSize, collOffset, anim, type);
        this.color = null;
    }

    public LevelObjectBackground(Level level, Rectangle rect,
            Dimension collSize, Vector2D collOffset, Color color,
            String type) {
        super(level, rect, collSize, collOffset, LevelObjectAnim.COLOR, type);
        this.color = color;
    }

    @Override
    public int getRenderPriority() {
        int winHeight = getLevel().getEditor().getCanvas().getHeight();
        return (int) (getLevel().getCamera().getRectangle().y) - winHeight;
    }

    @Override
    public boolean render(Graphics gfx, Camera camera) {
        KvadratikEditor gf = getLevel().getEditor();
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
