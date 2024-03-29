package com.github.inc0grepoz.kvad.entities;

import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.awt.CanvasRenderer;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class Camera extends Entity {

    public Way moveDirection;

    public Camera(Level level) {
        super(level, new Rectangle(0, 0, 0, 0), null, null);
        super.speed = 20;
    }

    public void scale(CanvasRenderer canvas) {
        Rectangle cam = getRectangle();
        cam.width = KvadratikEditor.INSTANCE.getWidth();
        cam.height = KvadratikEditor.INSTANCE.getHeight();
    }

    public void focus(Entity entity) {
        CanvasRenderer canvas = getLevel().getEditor().getCanvas();
        Rectangle cam = getRectangle();
        Rectangle ent = entity.getRectangle();
        cam.x = ent.x + (ent.width - canvas.getWidth()) / 2;
        cam.y = ent.y + (ent.height - canvas.getHeight()) / 2;
    }

}
