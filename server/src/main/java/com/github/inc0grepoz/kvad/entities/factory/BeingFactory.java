package com.github.inc0grepoz.kvad.entities.factory;

import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.JSON;

import lombok.Getter;

public class BeingFactory {

    private final @Getter String[] types;
    private final BeingTemplate[] templates;

    {
        String beingsJson = KvadratikServer.ASSETS.textFile("assets/beings/beings.json");
        templates = JSON.fromJsonBeingTemplates(beingsJson);
        types = Stream.of(templates).map(BeingTemplate::getType).toArray(String[]::new);
    }

    public boolean hasType(String type) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                return true;
            }
        }
        return false;
    }

    public BeingTemplate getTemplate(String type) {
        for (int i = 0; i < templates.length; i++) {
            if (templates[i].getType().equals(type)) {
                return templates[i];
            }
        }
        return null;
    }

    public Being create(String type, Level level, Point point) {
        return getTemplate(type).create(level, point);
    }

    public Player createPlayer(Connection connection, String name,
                               Level level, Point point, String type) {
        return getTemplate(type).createPlayer(connection, name, level, point);
    }

    public Player createPlayerCopy(Player player, Level level, Point point) {
        return getTemplate(player.getType()).createPlayer(player.getConnection(),
                player.getName(), level, point);
    }

}
