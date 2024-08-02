package com.github.inc0grepoz.kvad.entities.being;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerPreset {

    private final Level level;
    private final Point point;
    private final String type;

    public Player spawn(Connection connection, String name) {
        return KvadratikServer.BEING_FACTORY.createPlayer(connection, name, level, point, type);
    }

    public Player spawnCopy(Player player) {
        return KvadratikServer.BEING_FACTORY.createPlayerCopy(player, level, point);
    }

}
