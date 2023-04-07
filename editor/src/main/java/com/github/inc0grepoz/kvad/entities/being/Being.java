package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

import lombok.Getter;

public class Being extends Renderable {

    private static int lastId;

    private @Getter final int id;
    private final Vector prevMove = new Vector();

    private Anim anim = Anim.IDLE_S;
    private long animExpiry; // 0 for infinite duration
    private int animSpriteIndex;

    private @Getter String type;

    public Being(Level level, Rectangle rect, Dimension collSize, Vector collOffset,
            String type, int id) {
        super(level, rect, collSize, collOffset);
        super.collide = true;
        this.id = id < 0 ? ++lastId : id;
        this.type = type;
    }

    public Being(Level level, Rectangle rect, Dimension collSize, Vector collOffset,
            String type) {
        this(level, rect, collSize, collOffset, type, -1);
    }

    public boolean isMoving() {
        return prevMove.x != 0 || prevMove.y != 0;
    }

    public void playIdleAnim() {
        Anim nextAnim;
        switch (anim.way) {
            case W:
                nextAnim = Anim.IDLE_W;
                break;
            case A:
                nextAnim = Anim.IDLE_A;
                break;
            case D:
                nextAnim = Anim.IDLE_D;
                break;
            default:
                nextAnim = Anim.IDLE_S;
        }
        applyAnim(nextAnim);
    }

    public void morph(String type) {
        this.type = type;
    }

    public void applyAnim(Anim anim) {
        if (anim != null && this.anim != anim) {
            this.anim = anim;
            animSpriteIndex = 0;
            animExpiry = System.currentTimeMillis() + anim.delay;
        }
    }

    public Anim getAnim() {
        return anim;
    }

    public BufferedImage getSprite() {
        BufferedImage[] images = anim.getImages(type);
        if (System.currentTimeMillis() > animExpiry) {
            animSpriteIndex += images.length < animSpriteIndex + 2 ?
                    -animSpriteIndex : 1;
            animExpiry = System.currentTimeMillis() + anim.delay;
        }
        return images[animSpriteIndex];
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(getSprite(), x, y, width, height, getLevel().getEditor().getCanvas());
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        int x = (int) (rect.getCenterX() - cam.x), y = rect.y + rect.height - cam.y;
        int width = gfx.getFontMetrics().stringWidth(type);
        x -= width / 2;
        y += 15;
        gfx.drawString(type, x, y);
    }

    @Override
    public void delete() {
        Level lvl = getLevel();
        if (id != lvl.getPlayer().getId()) {
            lvl.getBeings().removeIf(b -> b.getId() == id);
        }
    }

}
