package com.github.inc0grepoz.kvad.entities.level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.Entity;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.being.PlayerPreset;
import com.github.inc0grepoz.kvad.protocol.PacketUtil;
import com.github.inc0grepoz.kvad.server.KvadratikServer;

import lombok.Getter;
import lombok.Setter;

public class Level {

    private final @Getter KvadratikServer server;
    private final @Getter String name;
    private final String json;

    private final @Getter List<LevelObject> levelObjects = new ArrayList<>();
    private final @Getter List<Being> beings = new ArrayList<>();

    private @Getter @Setter PlayerPreset playerPreset;

    public Level(KvadratikServer kvad, String name, String json) {
        this.server = kvad;
        this.name = name;
        this.json = json;
    }

    @Override
    public String toString() {
        return json;
    }

    public void join(Connection cxn, Level level, String playerName) {
        Player present = server.unbindPlayer(playerName);
        Player player = present == null ? playerPreset.spawn(cxn, playerName)
                : playerPreset.spawnCopy(present);

        // Sending the level data and all beings
        PacketUtil util = server.packetUtil;
        util.outLevel(player, level);
        util.outBeingSpawnForAll(player);
        level.getBeings().forEach(being -> util.outBeingSpawn(player, being));

        util.outTransferControl(player, player);
    }

    public Stream<? extends Entity> entitiesStream() {
        return Stream.concat(levelObjects.stream(), beings.stream());
    }

}
