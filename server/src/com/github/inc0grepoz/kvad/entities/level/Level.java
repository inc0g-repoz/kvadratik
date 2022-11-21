package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.XML;
import com.github.inc0grepoz.kvad.utils.XMLSection;

public class Level {

    private final KvadratikServer kvad;
    private final XML xml;

    private String name;
    private ArrayList<LevelObject> levelObjects = new ArrayList<>();
    private ArrayList<Being> beings = new ArrayList<>();
    private int[] pRect;

    public Level(KvadratikServer kvad, XML xml) {
        this.kvad = kvad;
        this.xml = xml;
        load();
    }

    @Override
    public String toString() {
        return xml.toString();
    }

    public String getName() {
        return name;
    }

    public KvadratikServer getGame() {
        return kvad;
    }

    public ArrayList<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public ArrayList<Being> getBeings() {
        return beings;
    }

    public Stream<? extends Entity> entitiesStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public int[] getSpawnRect() {
        return pRect;
    }

    public void load() {
        levelObjects.clear();
        beings.clear();
        name = xml.getString("root.name");

        pRect = xml.getIntArray("root.player.rectangle");

        XMLSection loSect = xml.getSection("root.levelObjects");
        loSect.getKeys().forEach(key -> {
            LevelObject lo = null;
            String typeStr = loSect.getString(key + ".type");
            int[] rect = loSect.getIntArray(key + ".rectangle");
            switch (typeStr) {
                case "Background": {
                    String animStr = loSect.getString(key + ".anim");
                    if (animStr != null) {
                        LevelObjectAnim anim = LevelObjectAnim.valueOf(animStr);
                        lo = new LevelObjectBackground(this, rect, anim);
                    } else {
                        int[] rgb = loSect.getIntArray(key + ".color");
                        Color color = RGB.get(rgb);
                        lo = new LevelObjectBackground(this, rect, color);
                    }
                    break;
                }
                case "Animated": {
                    String animStr = loSect.getString(key + ".anim");
                    LevelObjectAnim anim = LevelObjectAnim.valueOf(animStr);
                    lo = new LevelObjectAnimated(this, rect, anim);
                    break;
                }
                default: {
                    lo = new LevelObjectRectangle(this, rect);
                }
            }
            lo.collide = loSect.getBoolean(key + ".collide");
            levelObjects.add(lo);
        });
    }

}
