package com.github.inc0grepoz.kvad.utils;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

    public static Level fromJsonLevel(String json, boolean mpMode) {
        KvadratikGame.BEING_FACTORY.validatePreload();
        KvadratikGame.OBJECT_FACTORY.validatePreload();

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

            Being player = KvadratikGame.BEING_FACTORY.create(jPlayerType, level, point);
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
            KvadratikGame.OBJECT_FACTORY.create(type, level, point);
        });

        return level;
    }

}
