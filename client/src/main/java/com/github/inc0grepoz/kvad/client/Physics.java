package com.github.inc0grepoz.kvad.client;

import com.github.inc0grepoz.kvad.common.entities.Camera;
import com.github.inc0grepoz.kvad.common.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.common.entities.being.Being;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.TimeGap;

public class Physics {

    private final Session session;

    public Physics(Session session) {
        this.session = session;
    }

    public void tick() {
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
    }

}
