package com.github.inc0grepoz.kvad.client.gui.menu;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.common.awt.Canvas;

import lombok.Getter;
import lombok.Setter;

public abstract class CanvasItem {

    protected final @Getter String caption;
    protected final Rectangle rect;
    protected @Getter Runnable callback;
    protected @Getter @Setter boolean selected;

    public CanvasItem(String caption, Rectangle rect) {
        this.caption = caption;
        this.rect = rect;
    }

    public abstract boolean isAtPoint(int x, int y);

    public abstract void alignX(Canvas canvas, Graphics buff, int y);

    public abstract void render(Graphics gfx);

}
