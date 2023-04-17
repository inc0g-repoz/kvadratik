package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public abstract class CanvasMenu {

    public static final int ITEM_GAP = 40;

    private final @Getter List<CanvasButton> buttons = new ArrayList<>();

    public void render(Graphics gfx) {
        buttons.forEach(b -> b.render(gfx));
    }

    public void addButton(String caption, Runnable callback) {
        buttons.add(new CanvasButton(caption, callback));
    }

    public void alignItems(Canvas canvas, Graphics buff) {
        int y = (canvas.getHeight() - (buttons.size() - 1) * ITEM_GAP) / 2;
        for (CanvasButton cb : buttons) {
            cb.alignX(canvas, buff, y);
            y += ITEM_GAP;
        }
    }

    public void click(int x, int y) {
        for (CanvasButton button : buttons) {
            if (button.isAtPoint(x, y)) {
                button.getCallback().run();
                break;
            }
        }
    }

    public void hover(int x, int y) {
        for (CanvasButton button : buttons) {
            button.setSelected(button.isAtPoint(x, y));
        }
    }

    public abstract String getTitle();

}
