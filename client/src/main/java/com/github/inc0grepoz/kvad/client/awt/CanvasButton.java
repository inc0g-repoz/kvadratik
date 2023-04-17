package com.github.inc0grepoz.kvad.client.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import lombok.Getter;
import lombok.Setter;

public class CanvasButton {

    private final @Getter String caption;
    private final @Getter Runnable callback;
    private final Rectangle rect = new Rectangle(0, 0, 100, 20);
//  private @Getter @Setter Color buttonColor = Color.WHITE;
    private @Getter @Setter boolean selected;
    private int capX, capY;

    public CanvasButton(String caption, Runnable callback) {
        this.caption = caption;
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

    public boolean isAtPoint(int x, int y) {
        return rect.contains(x, y);
    }

}
