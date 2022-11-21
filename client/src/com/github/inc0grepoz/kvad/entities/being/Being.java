package com.github.inc0grepoz.kvad.entities.being;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Vector;

public class Being extends Renderable {

    private static int lastId;

    public boolean sprint;

    private final BeingType type;
    private final int id;
    private final Vector lastMove = new Vector();

    private Anim anim = Anim.IDLE_S;
    private long animExpiry; // 0 for infinite duration
    private int animSpriteIndex;

    private String name;

    public Being(Level level, int[] rect, int[] coll, BeingType type, int id) {
        super(level, rect, coll);
        super.collide = true;
        super.moveSpeed = 4;
        this.id = id < 0 ? ++lastId : id;
        this.type = type;
    }

    public Being(Level level, int[] rect, int[] coll, BeingType type) {
        this(level, rect, coll, type, -1);
    }

    public Being(Level level, int[] rect, BeingType type, int id) {
        this(level, rect, null, type, id);
    }

    public Being(Level level, int[] rect, BeingType type) {
        this(level, rect, type, -1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isMoving() {
        return lastMove.x != 0 || lastMove.y != 0;
    }

    public void moveOn() {
        if (move) {
            int moveX = anim.way.x * moveSpeed;
            int moveY = anim.way.y * moveSpeed;

            if (sprint) {
                moveX *= 3;
                moveY *= 3;
            }

            boolean moved = move(moveX, moveY);

            if (moved) {
                if (lastMove.x != moveX || lastMove.y != moveY) {
                    getLevel().getGame().getClient().getPacketUtil()
                            .rectThenSpeed(this, moveX, moveY);
                }

                lastMove.x = moveX;
                lastMove.y = moveY;
            } else {
                playIdleAnim();
            }
        } else {
            lastMove.x = 0;
            lastMove.y = 0;

            getLevel().getGame().getClient().getPacketUtil()
                    .rectThenSpeed(this, 0, 0);
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

    public void applyAnim(Anim anim) {
        if (anim != null && this.anim != anim) {
            this.anim = anim;
            animSpriteIndex = 0;
            animExpiry = System.currentTimeMillis() + anim.delay;

            // Queue an anim packet
            getLevel().getGame().getClient().getPacketUtil().anim();
        }
    }

    public Anim getAnim() {
        return anim;
    }

    public BeingType getType() {
        return type;
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
        graphics.drawImage(getSprite(), x, y, width, height, getLevel().getGame().getCanvas());
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        if (name != null && this != getLevel().getPlayer()) {
            int x = (int) (rect.getCenterX() - cam.x), y = rect.y - cam.y;
            int width = gfx.getFontMetrics().stringWidth(name);
            x -= width / 2;
            y -= 5;
            gfx.drawString(name, x, y);
        }
    }

}
