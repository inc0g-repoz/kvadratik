package com.github.inc0grepoz.kvad.editor;

import com.github.inc0grepoz.kvad.editor.Controls.Key;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class ControlsHandler {

    private final Controls controls;

    public ControlsHandler(Controls controls) {
        this.controls = controls;
    }

    public void onKeyPressed(Key key) {
        KvadratikGame game = controls.getGame();
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {
            Being player = level.getPlayer();

            if (player != null) {
                // Defining the camera moving direction
                Camera camera = level.getCamera();
                camera.move = Controls.isCameraMoving();
                if (camera.move) {
                    switch (key) {
                        case SELECT_UP:
                            camera.moveDirection = Way.W;
                            break;
                        case SELECT_DOWN:
                            camera.moveDirection = Way.S;
                            break;
                        case SELECT_LEFT:
                            camera.moveDirection = Way.A;
                            break;
                        case SELECT_RIGHT:
                            camera.moveDirection = Way.D;
                            break;
                        default:
                    }
                }
            }
        }
    }

    public void onKeyReleased(Key key) {
        Level level = controls.getGame().getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {
            Camera camera = level.getCamera();
            camera.move = Controls.isCameraMoving();
            if (!camera.move) {
                camera.moveDirection = null;
            }
        }
    }

}
