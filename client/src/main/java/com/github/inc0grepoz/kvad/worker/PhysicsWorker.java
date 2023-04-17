package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.client.Session;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.TimeGap;

public class PhysicsWorker extends Worker {

    private final KvadratikGame game;
    private final Session session;

    public PhysicsWorker(KvadratikGame game, Session session, long delay) {
        super(delay);
        this.game = game;
        this.session = session;
    }

    @Override
    protected void work() {
        if (game.getCanvas().getWorker().isExecuting()) {
            return;
        }

        Level level = session.getLevel();
        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        // Moving the beings
        long gap = TimeGap.get();
        level.getBeings().forEach(b -> b.moveOn(gap));

        // Moving the camera
        Being player = level.getPlayer();
        if (player != null) {
            Camera camera = level.getCamera();
            if (camera.mode == CameraMode.FOLLOW) {
                camera.focus(player);
            } else if (camera.mode == CameraMode.FREE) {
                camera.move(camera.moveDirection);
            }
        }

        // TODO: AI

        if (game.getSession() != session) {
            kill();
        }
    }

}
