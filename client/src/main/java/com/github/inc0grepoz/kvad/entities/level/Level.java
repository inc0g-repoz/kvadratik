package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.being.Being;

import lombok.Getter;
import lombok.Setter;

public class Level {

    private final @Getter String name;
    private final @Getter Camera camera;

    private @Getter @Setter Being player;
    private @Getter List<LevelObject> levelObjects = new ArrayList<>();
    private @Getter List<Being> beings = new ArrayList<>();

    public Level(String name) {
        this.name = name;
        camera = new Camera(this);
    }

    public Stream<? extends Renderable> renEntsStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public Stream<? extends Renderable> renEntsStreamSorted() {
        return renEntsStream().sorted((r1, r2) -> r1.getRenderPriority() - r2.getRenderPriority());
    }

}
