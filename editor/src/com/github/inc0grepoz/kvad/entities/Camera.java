package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.editor.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class Camera extends Entity {

    public Way moveDirection;

    public Camera(Level level) {
        super(level, new Rectangle(0, 0, 0, 0), null, null);
        super.speed = 20;
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
