package com.github.inc0grepoz.kvad;

import java.awt.Rectangle;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Controls.Key;
import com.github.inc0grepoz.Worker;
import com.github.inc0grepoz.kvad.entities.Anim;
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
        double speed = 2;
        if (ctrls.isPressed(Key.SPRINT)) {
            speed *= 3;
        }

        Level level = game.getLevel();
        Player player = level.getPlayer();
        Rectangle pRect = player.getRectangle();

        if (ctrls.isPressed(Key.MOVE_FORWARD)) {
            pRect.y -= speed;
            player.applyAnim(Anim.PLAYER_WALK_W);
        } else if (ctrls.isPressed(Key.MOVE_LEFT)) {
            pRect.x -= speed;
            player.applyAnim(Anim.PLAYER_WALK_A);
        } else if (ctrls.isPressed(Key.MOVE_BACK)) {
            pRect.y += speed;
            player.applyAnim(Anim.PLAYER_WALK_S);
        } else if (ctrls.isPressed(Key.MOVE_RIGHT)) {
            pRect.x += speed;
            player.applyAnim(Anim.PLAYER_WALK_D);
        } else {
            player.applyAnim(Anim.PLAYER_IDLE_S);
        }
    }

}
