package com.github.inc0grepoz.kvad.client.gui.menu;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.common.awt.Canvas;

public class CanvasItemButton extends CanvasItem {

    private int capX, capY;

    public CanvasItemButton(String caption, Runnable callback) {
        super(caption, new Rectangle(0, 0, 100, 20));
        this.callback = callback;
    }

    public void render(Graphics gfx) {
        if (rect.x == 0 || rect.y == 0) {
            return;
        }

        Color canvasColor = gfx.getColor();
        gfx.setColor(selected ? Color.YELLOW : Color.WHITE);
        gfx.drawRect(rect.x, rect.y, rect.width, rect.height);
        gfx.drawString(caption, capX, capY);
        gfx.setColor(canvasColor);
    }

    @Override
    public boolean isAtPoint(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public void alignX(Canvas canvas, Graphics buff, int y) {
        int rectHW = rect.width / 2;
        int rectHH = rect.height / 2;
        rect.x = canvas.getWidth() / 2 - rectHW;
        rect.y = y - rectHH;

        FontMetrics font = buff.getFontMetrics();
        int capHW = font.stringWidth(caption) / 2;
        int capHH = font.getAscent() / 2;
        capX = rect.x + rectHW - capHW;
        capY = rect.y + rectHH + capHH;
    }

}
