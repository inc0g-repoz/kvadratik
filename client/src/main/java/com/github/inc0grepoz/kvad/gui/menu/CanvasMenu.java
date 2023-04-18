package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.kvad.client.KvadratikGame;

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
                KvadratikGame.ASSETS.playSound("assets/sounds/gui_mouse_click.wav");
                break;
            }
        }
    }

    public void hover(int x, int y) {
        for (CanvasButton button : buttons) {
            boolean wasSelected = button.isSelected();
            button.setSelected(button.isAtPoint(x, y));

            if (!wasSelected && button.isSelected()) {
                KvadratikGame.ASSETS.playSound("assets/sounds/gui_mouse_hover.wav");
            }
        }
    }

    public abstract String getTitle();

}
