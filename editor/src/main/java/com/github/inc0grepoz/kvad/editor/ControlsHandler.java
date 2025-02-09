package com.github.inc0grepoz.kvad.editor;

import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.Camera;
import com.github.inc0grepoz.kvad.common.entities.Renderable;
import com.github.inc0grepoz.kvad.common.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.editor.Controls.Key;

public class ControlsHandler {

    private final Controls controls;

    public ControlsHandler(Controls controls) {
        this.controls = controls;
    }

    public void onKeyPressed(Key key) {
        KvadratikEditor editor = controls.getEditor();
        Level level = editor.getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {

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

            Selection sel = editor.getSelection();

            if (key == Key.DELETE) {
                switch (sel.getMode()) {
                    case GRID: {
                        Rectangle selRect = sel.selGrid.rect;
                        level.renEntsStreamReversed()
                                .filter(e -> e.getRectangle().intersects(selRect))
                                .forEach(Renderable::delete);
                        break;
                    }
                    case POINT: {
                        Renderable target = sel.selTar.getTarget();
                        if (target != null) {
                            sel.selTar.clearSelection();
                            target.delete();
                        }
                        break;
                    }
                    default:
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
