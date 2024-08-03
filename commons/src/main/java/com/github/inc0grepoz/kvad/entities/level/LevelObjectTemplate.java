package com.github.inc0grepoz.kvad.entities.level;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.github.inc0grepoz.kvad.awt.geom.Dimension;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.awt.geom.Rectangle;
import com.github.inc0grepoz.kvad.entities.factory.RenderableTemplate;
import com.github.inc0grepoz.kvad.utils.Platform;
import com.github.inc0grepoz.kvad.utils.Vector2D;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LevelObjectTemplate implements RenderableTemplate {

    private static final Icon MISSING = new ImageIcon(Platform.getInstance().getAssetsProvider()
            .image("assets/editor/icons/missing.png").getScaledInstance(32, 32, Image.SCALE_FAST));

    private String name;
    private LevelObjectType type;
    private Dimension size, colliderSize;
    private Vector2D colliderOffset;
    private boolean collide;
    private int[] color;

    @Override
    public LevelObject create(Level level, Point point) {
        Rectangle rect = new Rectangle(point, size);
        LevelObject lo = type.getFactory().supply(this, level, rect);
        lo.collide = collide;
        return lo;
    }

    @Override
    public Icon getListIcon() {
        LevelObjectAnim anim = LevelObjectAnim.valueOf(name);
        if (anim == null) {
            return MISSING;
        }

        Image[] images = anim.getImages();

        if (images != null && images.length != 0) {
            Image image = images[0].getScaledInstance(32, 32, Image.SCALE_FAST);
            return new ImageIcon(image);
        }

        return MISSING;
    }

}
