package com.github.inc0grepoz.kvad.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.being.PlayerPreset;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateAnimated;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateBackground;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateRectangle;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.server.Settings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

    public static Settings fromJsonSettings(String json) {
        Map<String, Object> map = new HashMap<>();
        JsonObject jSettings = JsonParser.parseString(json).getAsJsonObject();
        map.put("assetsLink", jSettings.get("assetsLink").getAsString());
        map.put("timeout", jSettings.get("timeout").getAsInt());
        return new Settings(map);
    }

    public static BeingTemplate[] fromJsonBeingTemplates(String json) {
        List<BeingTemplate> list = new LinkedList<>();
        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        for (String key : jLevel.keySet()) {
            JsonObject jTemplate = jLevel.getAsJsonObject(key);

            // Size
            JsonArray jSize = jTemplate.getAsJsonArray("size");
            Dimension size = new Dimension(
                    jSize.get(0).getAsInt(),
                    jSize.get(1).getAsInt());

            // Collider
            Dimension collSize = null;
            Vector collOffset = null;
            if (jTemplate.has("collider")) {
                JsonArray jColl = jTemplate.getAsJsonArray("collider");
                collSize = new Dimension(
                        jColl.get(0).getAsInt(),
                        jColl.get(1).getAsInt());
                collOffset = new Vector(
                        jColl.get(2).getAsInt(),
                        jColl.get(3).getAsInt());
            }

            BeingTemplate template = new BeingTemplate(key, size, collSize, collOffset);
            list.add(template);
        }
        return list.stream().toArray(BeingTemplate[]::new);
    }

    public static LevelObjectTemplate[] fromJsonLevelObjectTemplates(String json) {
        List<LevelObjectTemplate> list = new LinkedList<>();
        JsonObject jLevelObjectType = JsonParser.parseString(json).getAsJsonObject();
        for (String key : jLevelObjectType.keySet()) {
            JsonObject jTemplate = jLevelObjectType.getAsJsonObject(key);

            // Size & collider
            JsonArray jSize = jTemplate.getAsJsonArray("size");
            Dimension size = new Dimension(
                    jSize.get(0).getAsInt(),
                    jSize.get(1).getAsInt());

            // Collider
            Dimension collSize = null;
            Vector collOffset = null;
            boolean collide = false;
            if (jTemplate.has("collider")) {
                JsonArray jColl = jTemplate.getAsJsonArray("collider");
                collSize = new Dimension(
                        jColl.get(0).getAsInt(),
                        jColl.get(1).getAsInt());
                collOffset = new Vector(
                        jColl.get(2).getAsInt(),
                        jColl.get(3).getAsInt());
            }
            if (jTemplate.has("collide")) {
                collide = jTemplate.get("collide").getAsBoolean();
            }

            // Type
            String type = jTemplate.get("type").getAsString();

            // Type dependant
            LevelObjectTemplate template;

            switch (type) {
                case "Animated": {
                    LevelObjectAnim anim = LevelObjectAnim.valueOf(key);
                    template = new LevelObjectTemplateAnimated(key, size,
                            collSize, collOffset, collide, anim);
                    break;
                }
                case "Background": {
                    if (jTemplate.has("color")) {
                        JsonArray jColor = jTemplate.getAsJsonArray("color");
                        int[] rgb = new int[] {
                                jColor.get(0).getAsInt(),
                                jColor.get(1).getAsInt(),
                                jColor.get(2).getAsInt()};
                        Color color = RGB.get(rgb);
                        template = new LevelObjectTemplateBackground(key, size,
                                collSize, collOffset, collide, color);
                    } else {
                        LevelObjectAnim anim = LevelObjectAnim.valueOf(key);
                        template = new LevelObjectTemplateBackground(key, size,
                                collSize, collOffset, collide, anim);
                    }
                    break;
                }
                default: {
                    template = new LevelObjectTemplateRectangle(key, size);
                }
            }

            list.add(template);
        }

        return list.stream().toArray(LevelObjectTemplate[]::new);
    }

    public static LevelObjectAnim[] fromJsonLevelObjectAnim(String json) {
        JsonObject jAnims = JsonParser.parseString(json).getAsJsonObject();
        return jAnims.keySet().stream().map(LevelObjectAnim::new).collect(Collectors.toList())
                .stream().toArray(LevelObjectAnim[]::new);
    }

    public static Level fromJsonLevel(KvadratikServer kvad, String json) {
        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        String name = jLevel.get("name").getAsString();

        // Creating a player preset
        JsonObject jPlayer = jLevel.getAsJsonObject("player");
        String jPlayerType = jPlayer.get("beingType").getAsString();

        JsonArray jPlayerPoint = jPlayer.getAsJsonArray("point");
        Point pPoint = new Point(
                jPlayerPoint.get(0).getAsInt(),
                jPlayerPoint.get(1).getAsInt());
        PlayerPreset preset = new PlayerPreset(jPlayerType, pPoint);

        Level level = new Level(kvad, name, json, preset);

        // Adding some level objects
        JsonObject jLevelObjects = jLevel.getAsJsonObject("levelObjects");
        for (String key : jLevelObjects.keySet()) {
            JsonArray jLevelObject = jLevelObjects.getAsJsonArray(key);
            Point point = new Point(
                    jLevelObject.get(0).getAsInt(),
                    jLevelObject.get(1).getAsInt());
            KvadratikServer.OBJECT_FACTORY.create(key, level, point);
        }

        return level;
    }

}
