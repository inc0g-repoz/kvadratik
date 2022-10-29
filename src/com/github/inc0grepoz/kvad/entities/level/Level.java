package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Being;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.Player;
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

    public Stream<? extends Entity> entitiesStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public void load() {
        levelObjects.clear();
        beings.clear();

        xml.getMap().forEach((e, k) -> System.out.println(e + " = " + k));

        int[] pRect = xml.getIntArray("root.player.rectangle");
        player = new Player(pRect, this);
        beings.add(player);

        int[] cRect = xml.getIntArray("root.camera.rectangle");
        camera = new Camera(cRect, this);

        XMLSection loSect = xml.getSection("root.levelObjects");
        loSect.getKeys().forEach(key -> {
            LevelObject lo = null;
            int[] rect = loSect.getIntArray(key + ".rectangle");
            String typeStr = loSect.getString(key + ".type");
            switch (typeStr) {
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
            levelObjects.add(lo);
        });
    }

}
