package com.github.inc0grepoz.kvad.entities.being;

import com.github.inc0grepoz.kvad.entities.level.Level;

public class Player extends Being {

    public Player(int[] rect, Level level) {
        super(rect, level, BeingType.IOMOR);
    }

}
