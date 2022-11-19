package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.Controls;
import com.github.inc0grepoz.kvad.Controls.Key;
import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class PhysicsWorker extends Worker {

    private final KvadratikGame game;
    private final Controls controls;

    public PhysicsWorker(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
        controls = game.getControls();
    }

    @Override
    protected void work() {
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        // Moving the player
        Being player = level.getPlayer();
        if (player != null) {
            int speed = player.getMoveSpeed();
            boolean moved, sprint = controls.isPressed(Key.SPRINT);
            if (sprint) {
                speed *= 3;
            }
            Anim anim = null;
            if (controls.isPressed(Key.MOVE_UP)) {
                moved = player.move(0, -speed);
                anim = sprint ? Anim.RUN_W : Anim.WALK_W;
            } else if (controls.isPressed(Key.MOVE_LEFT)) {
                moved = player.move(-speed, 0);
                anim = sprint ? Anim.RUN_A : Anim.WALK_A;
            } else if (controls.isPressed(Key.MOVE_DOWN)) {
                moved = player.move(0, speed);
                anim = sprint ? Anim.RUN_S : Anim.WALK_S;
            } else if (controls.isPressed(Key.MOVE_RIGHT)) {
                moved = player.move(speed, 0);
                anim = sprint ? Anim.RUN_D : Anim.WALK_D;
            } else {
                moved = false;
            }
            if (!moved) {
                switch (player.getAnim().getWay()) {
                    case W:
                        anim = Anim.IDLE_W;
                        break;
                    case A:
                        anim = Anim.IDLE_A;
                        break;
                    case D:
                        anim = Anim.IDLE_D;;
                        break;
                    default:
                        anim = Anim.IDLE_S;
                }
            }
            player.applyAnim(anim);

            // Moving the camera
            Camera camera = level.getCamera();
            if (camera.getMode() == CameraMode.FOLLOW) {
                camera.focus(player);
            } else if (camera.getMode() == CameraMode.FREE) {
                if (controls.isPressed(Key.SELECT_UP)) {
                    camera.move(0, -5);
                }
                if (controls.isPressed(Key.SELECT_LEFT)) {
                    camera.move(-5, 0);
                }
                if (controls.isPressed(Key.SELECT_DOWN)) {
                    camera.move(0, 5);
                }
                if (controls.isPressed(Key.SELECT_RIGHT)) {
                    camera.move(5, 0);
                }
            }
        }

        // TODO: AI
    }

}
