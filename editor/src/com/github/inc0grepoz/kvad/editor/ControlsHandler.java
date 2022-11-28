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
        KvadratikEditor game = controls.getEditor();
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {
            Being player = level.getPlayer();

            if (player != null) {
                // Defining the camera moving direction
                Camera camera = level.getCamera();
                camera.move = Controls.isCameraMoving()
                        || Controls.isCameraMovingAlt();
                if (camera.move) {
                    switch (key) {
                        case MOVE_UP:
                            camera.moveDirection = Way.W;
                            break;
                        case MOVE_DOWN:
                            camera.moveDirection = Way.S;
                            break;
                        case MOVE_LEFT:
                            camera.moveDirection = Way.A;
                            break;
                        case MOVE_RIGHT:
                            camera.moveDirection = Way.D;
                            break;
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
        Level level = controls.getEditor().getLevel();

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
