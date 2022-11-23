package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingType;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.XML;
import com.github.inc0grepoz.kvad.utils.XMLSection;

public class Level {

    private final KvadratikGame game;
    private final XML xml;

    private String name;
    private Camera camera;
    private Being player;
    private ArrayList<LevelObject> levelObjects = new ArrayList<>();
    private ArrayList<Being> beings = new ArrayList<>();

    public Level(KvadratikGame game, XML xml, boolean initPlayer) {
        this.game = game;
        this.xml = xml;
        load(initPlayer);
    }

    @Override
    public String toString() {
        return xml.toString();
    }

    public String getName() {
        return name;
    }

    public KvadratikGame getGame() {
        return game;
    }

    public Camera getCamera() {
        return camera;
    }

    public Being getPlayer() {
        return player;
    }

    public ArrayList<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public ArrayList<Being> getBeings() {
        return beings;
    }

    public Stream<? extends Renderable> entitiesStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public void setPlayerBeing(Being player) {
        this.player = player;
    }

    public void load(boolean initPlayer) {
        levelObjects.clear();
        beings.clear();
        name = xml.getString("root.name");

        if (initPlayer) {
            int[] pRect = xml.getIntArray("root.player.rectangle");
            int[] pColl = xml.getIntArray("root.player.collider");
            BeingType pType = BeingType.valueOf(xml.getString("root.player.beingType"));
            player = new Being(this, pRect, pColl, pType);
            beings.add(player);
        }

        int[] cRect = xml.getIntArray("root.camera.rectangle");
        camera = new Camera(this, cRect);

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
                    int[] coll = loSect.getIntArray(key + ".collider");
                    lo = new LevelObjectAnimated(this, rect, coll, anim);
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
