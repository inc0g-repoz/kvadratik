package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.being.Being;

public class Level {

    private final KvadratikEditor game;
    private final String name;
    private final Camera camera;

    private Being player;
    private List<LevelObject> levelObjects = new ArrayList<>();
    private List<Being> beings = new ArrayList<>();

    public Level(KvadratikEditor game, String name) {
        this.game = game;
        this.name = name;
        camera = new Camera(this);
    }

    public String getName() {
        return name;
    }

    public KvadratikEditor getEditor() {
        return game;
    }

    public Camera getCamera() {
        return camera;
    }

    public Being getPlayer() {
        return player;
    }

    public List<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public List<Being> getBeings() {
        return beings;
    }

    public Stream<? extends Renderable> renEntsStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public Stream<? extends Renderable> renEntsStreamSorted() {
        return renEntsStream().sorted((r1, r2) -> r1.getRenderPriority() - r2.getRenderPriority());
    }

    public Stream<? extends Renderable> renEntsStreamReversed() {
        return renEntsStream().sorted((r1, r2) -> r2.getRenderPriority() - r1.getRenderPriority());
    }

    public void setPlayerBeing(Being player) {
        this.player = player;
    }

}
