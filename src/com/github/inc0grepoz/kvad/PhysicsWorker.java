package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Controls.Key;
import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Anim;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class PhysicsWorker extends Worker {

    private final KvadratikGame game;

    public PhysicsWorker(KvadratikGame game, long delay) {
        super(delay);
        this.game = game;
    }

    @Override
    public void work() {
        Controls ctrls = game.getControls();
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code probably
            return;
        }

        int speed = 4;
        boolean sprint = ctrls.isPressed(Key.SPRINT);
        if (sprint) {
            speed *= 3;
        }

        // Moving the player
        Player player = level.getPlayer();
        boolean moved;
        if (ctrls.isPressed(Key.MOVE_UP)) {
            moved = player.move(0, -speed);
            player.applyAnim(sprint ? Anim.RUN_W : Anim.WALK_W);
        } else if (ctrls.isPressed(Key.MOVE_LEFT)) {
            moved = player.move(-speed, 0);
            player.applyAnim(sprint ? Anim.RUN_A : Anim.WALK_A);
        } else if (ctrls.isPressed(Key.MOVE_DOWN)) {
            moved = player.move(0, speed);
            player.applyAnim(sprint ? Anim.RUN_S : Anim.WALK_S);
        } else if (ctrls.isPressed(Key.MOVE_RIGHT)) {
            moved = player.move(speed, 0);
            player.applyAnim(sprint ? Anim.RUN_D : Anim.WALK_D);
        } else {
            moved = false;
        }

        if (!moved) {
            switch (player.getAnim().getWay()) {
                case W:
                    player.applyAnim(Anim.IDLE_W);
                    break;
                case A:
                    player.applyAnim(Anim.IDLE_A);
                    break;
                case D:
                    player.applyAnim(Anim.IDLE_D);
                    break;
                default:
                    player.applyAnim(Anim.IDLE_S);
            }
        }

        // Moving the camera
        Camera camera = level.getCamera();
        if (camera.getMode() == CameraMode.FREE) {
            if (ctrls.isPressed(Key.SELECT_UP)) {
                camera.move(0, -5);
            }
            if (ctrls.isPressed(Key.SELECT_LEFT)) {
                camera.move(-5, 0);
            }
            if (ctrls.isPressed(Key.SELECT_DOWN)) {
                camera.move(0, 5);
            }
            if (ctrls.isPressed(Key.SELECT_RIGHT)) {
                camera.move(5, 0);
            }
        }
    }

}
