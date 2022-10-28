package com.github.inc0grepoz.kvad.entities;

import java.awt.Rectangle;

import com.github.inc0grepoz.kvad.entities.level.Level;

public class Player extends Being {

    public Player(Rectangle rect, Level level) {
        super(rect, level, BeingType.IOMOR);
    }

}
