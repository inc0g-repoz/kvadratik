package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class Camera extends Entity {

    public static enum CameraMode {
        FOLLOW, FREE
    }

    private CameraMode mode = CameraMode.FOLLOW;

    public Camera(int[] rect, Level level) {
        super(rect, level);
    }

    public CameraMode getMode() {
        return mode;
    }

    public void setMode(CameraMode mode) {
        this.mode = mode;
    }

    public void scale(KvadratikGame game) {
        Rectangle cam = getRectangle();
        cam.width = game.getWidth();
        cam.height = game.getHeight();
    }

    public synchronized void focus(Entity entity) {
        KvadratikGame game = getLevel().getGame();
        Rectangle cam = getRectangle();
        Rectangle ent = entity.getRectangle();
        cam.x = ent.x + (ent.width - game.getWidth()) / 2;
        cam.y = ent.y + (ent.height - game.getHeight()) / 2;
    }

}
