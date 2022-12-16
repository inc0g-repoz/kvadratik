package com.github.inc0grepoz.kvad.chat;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Message {

    public static class Component {

        private final Color color;
        private final String text;

        private Component(String text, Color color) {
            this.color = color;
            this.text = text;
        }

    }

    private final ArrayList<Component> components = new ArrayList<>();

    public void render(Graphics gfx, int x, int y) {
        Color color = gfx.getColor();

        for (Component comp : components) {
            gfx.setColor(comp.color);
            gfx.drawString(comp.text, x, y);
            x += gfx.getFontMetrics().stringWidth(comp.text);
        }

        gfx.setColor(color);
    }

    public Message addComponent(String text, Color color) {
        Component comp = new Component(text, color == null ? Color.BLACK : color);
        components.add(comp);
        return this;
    }

    public Message addComponent(String text) {
        return addComponent(text, null);
    }

}
