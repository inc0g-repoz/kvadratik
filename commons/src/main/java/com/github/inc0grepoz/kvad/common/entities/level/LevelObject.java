package com.github.inc0grepoz.kvad.common.entities.level;

import java.awt.Graphics;

import com.github.inc0grepoz.kvad.common.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.common.entities.Renderable;
import lombok.Getter;

@Getter
public abstract class LevelObject extends Renderable {

    private LevelObjectTemplate template;

    public LevelObject(Level level, Rectangle rect, LevelObjectTemplate template) {
        super(level, rect, template.getColliderSize(), template.getColliderOffset());
        this.template = template;
    }

    @Override
    public void typeText(Graphics gfx, Rectangle cam, Rectangle rect) {
        if (selected) {
            String tempName = getType();
            int x = (int) (rect.getCenterX() - cam.x), y = (int) (rect.y + rect.height - cam.y);
            int width = gfx.getFontMetrics().stringWidth(tempName);
            x -= width / 2;
            y += 15;
            gfx.drawString(tempName, x, y);
        }
    }

    @Override
    public void delete() {
        getLevel().getLevelObjects().remove(this);
    }

    public String getType() {
        return template == null ? null : template.getName();
    }

}
