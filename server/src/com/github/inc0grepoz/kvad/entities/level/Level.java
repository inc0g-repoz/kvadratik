package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.PlayerPreset;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class Level {

    private final KvadratikServer kvad;
    private final String name, json;

    private List<LevelObject> levelObjects = new ArrayList<>();
    private List<Being> beings = new ArrayList<>();
    private PlayerPreset playerPreset;

    public Level(KvadratikServer kvad, String name, String json, PlayerPreset playerPreset) {
        this.kvad = kvad;
        this.name = name;
        this.json = json;
        this.playerPreset = playerPreset;
    }

    @Override
    public String toString() {
        return json.toString();
    }

    public String getName() {
        return name;
    }

    public KvadratikServer getGame() {
        return kvad;
    }

    public PlayerPreset getPlayerPreset() {
        return playerPreset;
    }

    public List<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public List<Being> getBeings() {
        return beings;
    }

    public Stream<? extends Entity> entitiesStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

}
