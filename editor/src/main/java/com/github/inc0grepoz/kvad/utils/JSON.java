package com.github.inc0grepoz.kvad.utils;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

    public static Level fromJsonLevel(String json, boolean mpMode) {
        Kvadratik kvad = Platform.getInstance();
        kvad.getBeingFactory().validatePreload();
        kvad.getLevelObjectFactory().validatePreload();

        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        String name = jLevel.get("name").getAsString();
        Level level = new Level(name);

        // Adding a player being
        if (!mpMode) {
            JsonObject jPlayer = jLevel.getAsJsonObject("player");
            String jPlayerType = jPlayer.get("type").getAsString();

            JsonArray jPlayerPoint = jPlayer.getAsJsonArray("point");
            Point point = new Point(
                    jPlayerPoint.get(0).getAsDouble(),
                    jPlayerPoint.get(1).getAsDouble());

            Being player = kvad.getBeingFactory().create(jPlayerType, level, point);
            level.getCamera().focus(player);
            level.setPlayer(player);
        }

        // Adding some level objects
        JsonArray jLevelObjects = jLevel.getAsJsonArray("levelObjects");
        jLevelObjects.forEach(elt -> {
            JsonObject jLevelObject = elt.getAsJsonObject();
            JsonArray jPoint = jLevelObject.get("point").getAsJsonArray();
            String type = jLevelObject.get("type").getAsString();

            Point point = new Point(
                    jPoint.get(0).getAsDouble(),
                    jPoint.get(1).getAsDouble());
            kvad.getLevelObjectFactory().create(type, level, point);
        });

        return level;
    }

    public static String toJsonLevel(Level level) {
        JsonObject jLevel = new JsonObject();
        jLevel.addProperty("name", level.getName());

        // Serializing the player
        Being player = level.getPlayer();
        Point playerPoint = player.getRectangle().getLocation();
        JsonObject jPlayer = new JsonObject();
        JsonArray jPlayerPoint = new JsonArray();
        jPlayerPoint.add(playerPoint.x);
        jPlayerPoint.add(playerPoint.y);
        jPlayer.addProperty("type", player.getType());
        jPlayer.add("point", jPlayerPoint);
        jLevel.add("player", jPlayer);

        // Serializing the level objects
        JsonArray jLevelObjects = new JsonArray();
        level.getLevelObjects().forEach(lo -> {
            JsonObject jLevelObject = new JsonObject();
            JsonArray jPoint = new JsonArray();

            Point point = lo.getRectangle().getLocation();
            jPoint.add(point.x);
            jPoint.add(point.y);

            jLevelObject.addProperty("type", lo.getName());
            jLevelObject.add("point", jPoint);
            jLevelObjects.add(jLevelObject);
        });
        jLevel.add("levelObjects", jLevelObjects);

        // Serializing the beings
        JsonArray jBeings = new JsonArray();
        level.getBeings().forEach(b -> {
            if (b == level.getPlayer()) {
                return;
            }

            JsonObject jBeing = new JsonObject();
            JsonArray jPoint = new JsonArray();

            Point point = b.getRectangle().getLocation();
            jPoint.add(point.x);
            jPoint.add(point.y);

            jBeing.addProperty("type", b.getType());
            jBeing.add("point", jPoint);
            jBeings.add(jBeing);
        });
        jLevel.add("beings", jBeings);

        Gson gb = new GsonBuilder().setPrettyPrinting().create();
        return gb.toJson(jLevel);
    }

}
