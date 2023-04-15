package com.github.inc0grepoz.kvad.entities;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.client.KvadratikCanvas;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class Camera extends Entity {

    public static enum CameraMode {
        FOLLOW, FREE
    }

    public Way moveDirection;
    public CameraMode mode = CameraMode.FOLLOW;

    public Camera(Level level) {
        super(level, new Rectangle(0, 0, 0, 0), null, null);
        super.speed = 20;
    }

    public void scale(KvadratikCanvas canvas) {
        Rectangle cam = getRectangle();
        cam.width = KvadratikGame.INSTANCE.getWidth();
        cam.height = KvadratikGame.INSTANCE.getHeight();
    }

    public void focus(Entity entity) {
        KvadratikCanvas canvas = KvadratikGame.INSTANCE.getCanvas();
        Rectangle cam = getRectangle();
        Rectangle ent = entity.getRectangle();
        cam.x = ent.x + (ent.width - canvas.getWidth()) / 2;
        cam.y = ent.y + (ent.height - canvas.getHeight()) / 2;
    }

}
