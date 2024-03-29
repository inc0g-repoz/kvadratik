package com.github.inc0grepoz.kvad.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateAnimated;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateBackground;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateRectangle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

    public static void fromJsonSettings(KvadratikGame game, String json) {
        JsonObject jSettings = JsonParser.parseString(json).getAsJsonObject();
        if (!game.getClient().isInfoProvided()) {
            String profileName = jSettings.getAsJsonObject("profile").get("name").getAsString();
            game.getClient().setNickname(profileName);
        }
    }

    public static BeingTemplate[] fromJsonBeingTemplates(String json) {
        List<BeingTemplate> list = new LinkedList<>();
        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        for (String key : jLevel.keySet()) {
            JsonObject jTemplate = jLevel.getAsJsonObject(key);

            // Size
            JsonArray jSize = jTemplate.getAsJsonArray("size");
            Dimension size = new Dimension(
                    jSize.get(0).getAsDouble(),
                    jSize.get(1).getAsDouble());

            // Collider
            Dimension collSize = null;
            Vector2D collOffset = null;
            if (jTemplate.has("collider")) {
                JsonArray jColl = jTemplate.getAsJsonArray("collider");
                collSize = new Dimension(
                        jColl.get(0).getAsDouble(),
                        jColl.get(1).getAsDouble());
                collOffset = new Vector2D(
                        jColl.get(2).getAsDouble(),
                        jColl.get(3).getAsDouble());
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

            // Size
            JsonArray jSize = jTemplate.getAsJsonArray("size");
            Dimension size = new Dimension(
                    jSize.get(0).getAsDouble(),
                    jSize.get(1).getAsDouble());

            // Collider
            Dimension collSize = null;
            Vector2D collOffset = null;
            boolean collide = false;
            if (jTemplate.has("collider")) {
                JsonArray jColl = jTemplate.getAsJsonArray("collider");
                collSize = new Dimension(
                        jColl.get(0).getAsDouble(),
                        jColl.get(1).getAsDouble());
                collOffset = new Vector2D(
                        jColl.get(2).getAsDouble(),
                        jColl.get(3).getAsDouble());
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
        List<LevelObjectAnim> list = new ArrayList<>();
        JsonObject jAnims = JsonParser.parseString(json).getAsJsonObject();

        for (String key : jAnims.keySet()) {
            JsonObject jTemplate = jAnims.getAsJsonObject(key);

            JsonArray jPaths = jTemplate.getAsJsonArray("paths");
            List<String> paths = new LinkedList<>();
            jPaths.forEach(jPath -> paths.add(jPath.getAsString()));

            int delay = jTemplate.get("delay").getAsInt();

            LevelObjectAnim anim = new LevelObjectAnim(delay, key,
                    paths.stream().toArray(String[]::new));
            list.add(anim);
        }

        return list.stream().toArray(LevelObjectAnim[]::new);
    }

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
