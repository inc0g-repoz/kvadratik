package com.github.inc0grepoz.kvad;

import java.awt.Rectangle;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Controls.Key;
import com.github.inc0grepoz.Worker;
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
            speed *= 2;
        }

        Level level = game.getLevel();
        Player player = level.getPlayer();
        Rectangle pRect = player.getRectangle();

        if (ctrls.isPressed(Key.MOVE_FORWARD)) {
            pRect.y -= speed;
        }
        if (ctrls.isPressed(Key.MOVE_LEFT)) {
            pRect.x -= speed;
        }
        if (ctrls.isPressed(Key.MOVE_BACK)) {
            pRect.y += speed;
        }
        if (ctrls.isPressed(Key.MOVE_RIGHT)) {
            pRect.x += speed;
        }
    }

}
