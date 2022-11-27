package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.editor.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class PhysicsWorker extends Worker {

    private final KvadratikGame game;

    public PhysicsWorker(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
    }

    @Override
    protected void work() {
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        // Moving the camera
        Camera camera = level.getCamera();
        camera.move(camera.moveDirection);
    }

}
