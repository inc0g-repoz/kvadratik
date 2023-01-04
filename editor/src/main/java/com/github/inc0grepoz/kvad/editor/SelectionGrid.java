package com.github.inc0grepoz.kvad.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

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

    public void locate(int x, int y) {
        int gridSize = getGridSize();
        rect.x = x - x % gridSize;
        rect.y = y - y % gridSize;
        rect.x -= x < 0 ? gridSize : 0;
        rect.y -= y < 0 ? gridSize : 0;
    }

    public void render(Graphics gfx, Camera camera) {
        Level level = editor.getLevel();
        if (level == null) {
            return;
        }

        Rectangle cam = camera.getRectangle();
        int x = rect.x - cam.x;
        int y = rect.y - cam.y;

        Color color = gfx.getColor();
        gfx.setColor(Color.RED);
        gfx.drawRect(x, y, rect.width, rect.height);
        gfx.setColor(color);
    }

    public void setGridSize(int x, int y) {
        rect.width = x;
        rect.height = y;
    }

    public void setGridSize(int value) {
        setGridSize(value, value);
    }

    public int getGridSize() {
        return rect.width;
    }

}
