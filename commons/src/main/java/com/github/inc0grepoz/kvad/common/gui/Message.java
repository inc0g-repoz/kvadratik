package com.github.inc0grepoz.kvad.common.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.github.inc0grepoz.kvad.common.utils.RGB;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {

    public static class Component {

        private final Color color;
        protected final String text;

        private Component(String text, Color color) {
            this.color = color;
            this.text = text;
        }

    }

    public static Message deserialize(String json) {
        JsonArray jMsg = JsonParser.parseString(json).getAsJsonArray();
        Message msg = new Message();
        jMsg.forEach(comp -> {
            JsonObject jComp = comp.getAsJsonObject();
            JsonArray jCol = jComp.getAsJsonArray("color");
            Color color = jCol == null ? null : RGB.get(
                            jCol.get(0).getAsInt(),
                            jCol.get(1).getAsInt(),
                            jCol.get(2).getAsInt());
            String text = jComp.get("text").getAsString();
            msg.addComponent(text, color);
        });
        return msg;
    }

    private final ArrayList<Component> components = new ArrayList<>();

    @Override
    public String toString() {
        JsonArray jArr = new JsonArray();

        for (Component comp: components) {
            JsonObject jComp = new JsonObject();
            jComp.addProperty("text", comp.text);

            JsonArray jCol = new JsonArray();
            jCol.add(comp.color.getRed());
            jCol.add(comp.color.getGreen());
            jCol.add(comp.color.getBlue());
            jComp.add("color", jCol);

            jArr.add(jComp);
        }

        return jArr.toString();
    }

    public void render(Graphics gfx, int x, int y) {
        Color color = gfx.getColor();

        for (Component comp: components) {
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
