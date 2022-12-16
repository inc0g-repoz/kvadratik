package com.github.inc0grepoz.kvad.chat;

import java.awt.*;
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

    public Message addComponent(String text, Color color) {
        Component comp = new Component(text, color == null ? Color.BLACK : color);
        components.add(comp);
        return this;
    }

    public Message addComponent(String text) {
        return addComponent(text, null);
    }

}
