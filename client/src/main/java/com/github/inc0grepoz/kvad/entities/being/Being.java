package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.Vector;
import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;

import lombok.Getter;

public class Being extends Renderable {

    private static int lastId;

    private final @Getter int id;
    private final Vector prevMove = new Vector();

    private @Getter Anim anim = Anim.IDLE_S;
    private long animExpiry; // 0 for infinite duration
    private int animSpriteIndex;

    private @Getter String name, type;

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

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean isMoving() {
        return prevMove.x != 0 || prevMove.y != 0;
    }

    public void moveOn() {
        boolean moved = move(anim.way, anim.moveSpeed);

        if (moved) {
            int moveX = anim.way.x * anim.moveSpeed;
            int moveY = anim.way.y * anim.moveSpeed;

            KvadratikGame.INSTANCE.getClient().getPacketUtil()
                    .outPoint(this, prevMove, moveX, moveY);

            prevMove.x = moveX;
            prevMove.y = moveY;
        } else {
            KvadratikGame.INSTANCE.getClient().getPacketUtil()
                    .outPoint(this, prevMove, 0, 0);

            prevMove.x = 0;
            prevMove.y = 0;

            playIdleAnim();
        }
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

            // Queue an anim packet
            KvadratikGame.INSTANCE.getClient().getPacketUtil().outAnim();
        }
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
        graphics.drawImage(getSprite(), x, y, width, height, KvadratikGame.INSTANCE.getCanvas());
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        if (name != null && this != getLevel().getPlayer()) {
            int x = (int) (rect.getCenterX() - cam.x), y = (int) (rect.y - cam.y);
            int width = gfx.getFontMetrics().stringWidth(name);
            x -= width / 2;
            y -= 5;
            gfx.drawString(name, x, y);
        }
    }

}
