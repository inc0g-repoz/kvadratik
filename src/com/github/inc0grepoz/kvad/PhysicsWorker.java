package com.github.inc0grepoz.kvad;

import java.awt.Rectangle;

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

        double speed = 2;
        boolean sprint = ctrls.isPressed(Key.SPRINT);
        if (sprint) {
            speed *= 4;
        }

        Player player = level.getPlayer();
        Rectangle pRect = player.getRectangle();

        if (ctrls.isPressed(Key.MOVE_UP)) {
            pRect.y -= speed;
            player.applyAnim(sprint ? Anim.RUN_W : Anim.WALK_W);
        } else if (ctrls.isPressed(Key.MOVE_LEFT)) {
            pRect.x -= speed;
            player.applyAnim(sprint ? Anim.RUN_A : Anim.WALK_A);
        } else if (ctrls.isPressed(Key.MOVE_DOWN)) {
            pRect.y += speed;
            player.applyAnim(sprint ? Anim.RUN_S : Anim.WALK_S);
        } else if (ctrls.isPressed(Key.MOVE_RIGHT)) {
            pRect.x += speed;
            player.applyAnim(sprint ? Anim.RUN_D : Anim.WALK_D);
        } else {
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
                    break;
            }
        }

        // Moving the camera
        Camera camera = level.getCamera();
        Rectangle cRect = camera.getRectangle();

        if (camera.getMode() == CameraMode.FREE) {
            if (ctrls.isPressed(Key.SELECT_UP)) {
                cRect.y -= speed;
            } else if (ctrls.isPressed(Key.SELECT_LEFT)) {
                cRect.x -= speed;
            } else if (ctrls.isPressed(Key.SELECT_DOWN)) {
                cRect.y += speed;
            } else if (ctrls.isPressed(Key.SELECT_RIGHT)) {
                cRect.x += speed;
            }
        }
    }

}
