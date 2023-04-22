package com.github.inc0grepoz.kvad.gui.menu;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.kvad.client.KvadratikGame;

import lombok.Getter;
import lombok.Setter;

public abstract class CanvasMenu {

    public static final int ITEM_GAP = 40;

    private final @Getter List<CanvasItem> items = new ArrayList<>();

    private @Getter @Setter CanvasMenu parent;
    private CanvasItemTextField activeTextField;

    public void render(Graphics gfx) {
        items.forEach(b -> b.render(gfx));
    }

    public CanvasItemButton addButton(String caption, Runnable callback) {
        CanvasItemButton cib = new CanvasItemButton(caption, callback);
        items.add(cib);
        return cib;
    }

    public CanvasItemTextField addTextField(String caption, String content) {
        CanvasItemTextField citf = new CanvasItemTextField(caption, content);
        citf.callback = () -> activeTextField = citf;
        items.add(citf);
        return citf;
    }

    public void alignItems(Canvas canvas, Graphics buff) {
        int y = (canvas.getHeight() - (items.size() - 1) * ITEM_GAP) / 2;
        for (CanvasItem cb : items) {
            cb.alignX(canvas, buff, y);
            y += ITEM_GAP;
        }
    }

    public void click(int x, int y) {
        activeTextField = null;
        CanvasItem clicked = null;
        for (CanvasItem item : items) {
            if (item.isAtPoint(x, y)) {
                clicked = item;
                break;
            }
        }
        if (clicked == null) {
            return;
        }
        if (clicked instanceof CanvasItemButton) {
            clicked.getCallback().run();
        } else if (clicked instanceof CanvasItemTextField) {
            activeTextField = (CanvasItemTextField) clicked;
        }
        KvadratikGame.ASSETS.playSound("assets/sounds/gui_mouse_click.wav");
    }

    public void hover(int x, int y) {
        for (CanvasItem item : items) {
            boolean wasSelected = item.isSelected();
            item.setSelected(item.isAtPoint(x, y));

            if (!wasSelected && item.isSelected()) {
                KvadratikGame.ASSETS.playSound("assets/sounds/gui_mouse_hover.wav");
            }
        }
    }

    public void type(char c) {
        if (activeTextField != null) {
            activeTextField.content += c;
        }
    }

    public void eraseChar() {
        if (activeTextField.content.isEmpty()) {
            return;
        }
        activeTextField.content = activeTextField.content.substring(0, activeTextField.content.length() - 1);
    }

    public abstract String getTitle();

}
