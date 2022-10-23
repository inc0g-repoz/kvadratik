package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.Controls;
import com.github.inc0grepoz.Key;
import com.github.inc0grepoz.Worker;

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
        if (ctrls.isPressed(Key.MOVE_FORWARD)) {
            game.getLevel().getPlayer().getRectangle().y -= speed;
        }
        if (ctrls.isPressed(Key.MOVE_LEFT)) {
            game.getLevel().getPlayer().getRectangle().x -= speed;
        }
        if (ctrls.isPressed(Key.MOVE_BACK)) {
            game.getLevel().getPlayer().getRectangle().y += speed;
        }
        if (ctrls.isPressed(Key.MOVE_RIGHT)) {
            game.getLevel().getPlayer().getRectangle().x += speed;
        }
    }

}
