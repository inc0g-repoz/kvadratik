package com.github.inc0grepoz.kvad.server.utils;

import java.util.HashMap;
import java.util.Map;

import com.github.inc0grepoz.kvad.common.Kvadratik;
import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.server.entities.being.PlayerPreset;
import com.github.inc0grepoz.kvad.server.server.KvadratikServer;
import com.github.inc0grepoz.kvad.server.server.Settings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

    public static Settings fromJsonSettings(String json) {
        Map<String, Object> map = new HashMap<>();
        JsonObject jSettings = JsonParser.parseString(json).getAsJsonObject();
        map.put("assetsLink", jSettings.get("assetsLink").getAsString());
        map.put("port", jSettings.get("port").getAsInt());
        map.put("timeout", jSettings.get("timeout").getAsInt());
        return new Settings(map);
    }

    public static Level fromJsonLevel(KvadratikServer kvad, String json) {
        kvad.getLevelObjectFactory().validatePreload();
        kvad.getBeingFactory().validatePreload();

        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        String name = jLevel.get("name").getAsString();

        // Creating a player preset
        JsonObject jPlayer = jLevel.getAsJsonObject("player");
        String jPlayerType = jPlayer.get("type").getAsString();

        JsonArray jPlayerPoint = jPlayer.getAsJsonArray("point");
        Point pPoint = new Point(
                jPlayerPoint.get(0).getAsDouble(),
                jPlayerPoint.get(1).getAsDouble());

        Level level = new Level(kvad, name, json);
        PlayerPreset preset = new PlayerPreset(level, pPoint, jPlayerType);
        level.setPlayerPreset(preset);

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

}
