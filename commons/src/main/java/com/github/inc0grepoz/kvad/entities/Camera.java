package com.github.inc0grepoz.kvad.entities;

import java.awt.Canvas;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Platform;

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

    public void scale() {
        Rectangle cam = getRectangle();
        cam.width = Platform.getInstance().getWidth();
        cam.height = Platform.getInstance().getHeight();
    }

    public void focus(Entity entity) {
        Canvas canvas = Platform.getInstance().getCanvas();
        Rectangle cam = getRectangle();
        Rectangle ent = entity.getRectangle();
        cam.x = ent.x + (ent.width - canvas.getWidth()) / 2;
        cam.y = ent.y + (ent.height - canvas.getHeight()) / 2;
    }

}
