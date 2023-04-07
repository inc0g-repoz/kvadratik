package com.github.inc0grepoz.kvad.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateAnimated;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateBackground;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplateRectangle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSON {

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

    public static Level fromJsonLevel(KvadratikEditor game, String json, boolean mpMode) {
        JsonObject jLevel = JsonParser.parseString(json).getAsJsonObject();
        String name = jLevel.get("name").getAsString();
        Level level = new Level(game, name);

        // Adding a player being
        if (!mpMode) {
            JsonObject jPlayer = jLevel.getAsJsonObject("player");
            String jPlayerType = jPlayer.get("type").getAsString();

            JsonArray jPlayerPoint = jPlayer.getAsJsonArray("point");
            Point point = new Point(
                    jPlayerPoint.get(0).getAsInt(),
                    jPlayerPoint.get(1).getAsInt());

            Being player = KvadratikEditor.BEING_FACTORY.create(jPlayerType, level, point);
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
                    jPoint.get(0).getAsInt(),
                    jPoint.get(1).getAsInt());
            KvadratikEditor.OBJECT_FACTORY.create(type, level, point);
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

            jLevelObject.addProperty("type", lo.getType());
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
