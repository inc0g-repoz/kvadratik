package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.awt.Canvas;

import lombok.Getter;
import lombok.Setter;

public class CanvasItemTextField extends CanvasItem {

    protected @Getter String content = "";

    private @Getter @Setter boolean typing;
    private int capY, cntY;

    public CanvasItemTextField(String caption, String content) {
        super(caption, new Rectangle(0, 0, 200, 20));
        this.content = content == null ? "" : content;

        callback = () -> {
            typing = true;
        };
    }

    public void render(Graphics gfx) {
        if (rect.x == 0 || rect.y == 0) {
            return;
        }

        Color canvasColor = gfx.getColor();
        gfx.setColor(selected ? Color.YELLOW : Color.WHITE);
        gfx.drawRect(rect.x, rect.y, rect.width, rect.height);

        gfx.setColor(Color.WHITE);
        gfx.drawString(caption, rect.x, capY);
        gfx.drawString(content.concat("|") , rect.x + 4, cntY + 2);
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
        int fontDH = font.getAscent();
        capY = rect.y - fontDH;
        cntY = rect.y + fontDH;
    }

}
