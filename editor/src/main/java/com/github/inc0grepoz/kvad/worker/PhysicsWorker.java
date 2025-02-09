package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.common.entities.Camera;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.worker.Worker;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

public class PhysicsWorker extends Worker {

    private final KvadratikEditor editor;

    public PhysicsWorker(KvadratikEditor editor, long delay) {
        super(delay);
        this.editor = editor;
    }

    @Override
    protected void work() {
        Level level = editor.getLevel();

        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        // Moving the camera
        Camera camera = level.getCamera();
        camera.move(camera.moveDirection);
    }

}
