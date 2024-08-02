package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.being.Being;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Level {

    private final @Getter String name;
    private final @Getter Camera camera = new Camera(this);
    private final @Getter List<LevelObject> levelObjects = new ArrayList<>();
    private final @Getter List<Being> beings = new ArrayList<>();

    private @Getter @Setter String path;
    private @Getter @Setter Being player;

    public Stream<? extends Renderable> renEntsStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

    public Stream<? extends Renderable> renEntsStreamSorted() {
        return renEntsStream().sorted((r1, r2) -> r1.getRenderPriority() - r2.getRenderPriority());
    }

    public Stream<? extends Renderable> renEntsStreamReversed() {
        return renEntsStream().sorted((r1, r2) -> r2.getRenderPriority() - r1.getRenderPriority());
    }

}
