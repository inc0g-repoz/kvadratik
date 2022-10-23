package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.github.inc0grepoz.Game;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.LivingEntity;
import com.github.inc0grepoz.kvad.entities.Player;

public class Level {

    private Camera camera;
    private Player player = new Player(new Rectangle(0, 0, 50, 50));
    private ArrayList<LevelObject> levelObjects = new ArrayList<>();
    private ArrayList<LivingEntity> livingEntities = new ArrayList<>();

    public Level(Game game) {
        camera = new Camera(new Rectangle(0, 0, game.getWidth(), game.getHeight()));
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

    public ArrayList<LivingEntity> getLivingEntities() {
        return livingEntities;
    }

}
