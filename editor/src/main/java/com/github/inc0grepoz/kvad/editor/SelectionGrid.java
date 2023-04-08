package com.github.inc0grepoz.kvad.editor;

import java.awt.Color;
import java.awt.Graphics;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.factory.RenderableFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;

import lombok.Getter;
import lombok.Setter;

public class SelectionGrid {

    public final Rectangle rect = new Rectangle();

    private final KvadratikEditor editor;
    private @Getter @Setter RenderableFactory factory;

    public SelectionGrid(KvadratikEditor editor) {
        this.editor = editor;
        setGridSize(64);
    }

    public void locate(double x, double y) {
        double gridX = rect.width;
        double gridY = rect.height;
        rect.x = x - x % gridX;
        rect.y = y - y % gridY;
        rect.x -= x < 0 ? gridX : 0;
        rect.y -= y < 0 ? gridY : 0;
    }

    public void render(Graphics gfx, Camera camera) {
        Level level = editor.getLevel();
        if (level == null) {
            return;
        }

        Rectangle cam = camera.getRectangle();
        double x = rect.x - cam.x;
        double y = rect.y - cam.y;

        Color color = gfx.getColor();
        gfx.setColor(Color.RED);
        gfx.drawRect((int) x, (int) y, (int) rect.width, (int) rect.height);
        gfx.setColor(color);
    }

    public void setGridSize(double x, double y) {
        rect.width = x;
        rect.height = y;
    }

    public void setGridSize(int value) {
        setGridSize(value, value);
    }

}
