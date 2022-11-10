package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.utils.RGB;
import com.github.inc0grepoz.kvad.utils.XML;
import com.github.inc0grepoz.kvad.utils.XMLSection;

public class Level {

    private final KvadratikGame game;
    private final XML xml;

    private Camera camera;
    private Player player;
    private ArrayList<LevelObject> levelObjects = new ArrayList<>();
    private ArrayList<Being> beings = new ArrayList<>();

    public Level(KvadratikGame game, XML xml) {
        this.game = game;
        this.xml = xml;
        load();
    }

    @Override
    public String toString() {
        return xml.toString();
    }

    public KvadratikGame getGame() {
        return game;
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
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

    public void load() {
        levelObjects.clear();
        beings.clear();

        int[] pRect = xml.getIntArray("root.player.rectangle");
        player = new Player(pRect, this);
        beings.add(player);

        int[] cRect = xml.getIntArray("root.camera.rectangle");
        camera = new Camera(cRect, this);

        XMLSection loSect = xml.getSection("root.levelObjects");
        loSect.getKeys().forEach(key -> {
            LevelObject lo = null;
            String typeStr = loSect.getString(key + ".type");
            int[] rect = loSect.getIntArray(key + ".rectangle");
            boolean collide = loSect.getBoolean(key + ".collide");
            switch (typeStr) {
                case "Background": {
                    String animStr = loSect.getString(key + ".anim");
                    if (animStr != null) {
                        LevelObjectAnim anim = LevelObjectAnim.valueOf(animStr);
                        lo = new LevelObjectBackground(rect, this, anim);
                    } else {
                        int[] rgb = loSect.getIntArray(key + ".color");
                        Color color = RGB.get(rgb);
                        lo = new LevelObjectBackground(rect, this, color);
                    }
                    break;
                }
                case "Animated": {
                    String animStr = loSect.getString(key + ".anim");
                    LevelObjectAnim anim = LevelObjectAnim.valueOf(animStr);
                    lo = new LevelObjectAnimated(rect, this, anim);
                    break;
                }
                default: {
                    lo = new LevelObjectRectangle(rect, this);
                }
            }
            lo.setCollidable(collide);
            levelObjects.add(lo);
        });
    }

}
