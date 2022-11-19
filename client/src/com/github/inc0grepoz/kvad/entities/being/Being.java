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

    @Override
    public boolean move(int x, int y) {
        boolean moved = super.move(x, y);

        if (moved) {
            if (lastMove.x != x || lastMove.y != y) {
                getLevel().getGame().getClient().getPacketUtil()
                        .rectThenSpeed(this, x, y);
            }

            lastMove.x = x;
            lastMove.y = y;
        }

        return moved;
    }

    public void idle() {
        lastMove.x = 0;
        lastMove.y = 0;

        getLevel().getGame().getClient().getPacketUtil()
                .rectThenSpeed(this, 0, 0);
    }

    public void applyAnim(Anim anim) {
        if (this.anim != anim) {
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
        if (name != null) {
            int x = (int) (rect.getCenterX() - cam.x), y = rect.y - cam.y;
            int width = gfx.getFontMetrics().stringWidth(name);
            x -= width / 2;
            y -= 5;
            gfx.drawString(name, x, y);
        }
    }

}
