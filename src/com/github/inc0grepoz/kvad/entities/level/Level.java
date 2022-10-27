package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Being;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.Player;

public class Level {

    private final KvadratikGame game;
    private final Camera camera;

    private Player player = new Player(new Rectangle(65, 30, 32, 32), this);
    private ArrayList<LevelObject> levelObjects = new ArrayList<>();
    private ArrayList<Being> beings = new ArrayList<>();

    public Level(KvadratikGame game) {
        this.game = game;
        camera = new Camera(new Rectangle(0, 0, game.getWidth(), game.getHeight()), this);

        // Player is an instance of Being
        beings.add(player);
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

}
