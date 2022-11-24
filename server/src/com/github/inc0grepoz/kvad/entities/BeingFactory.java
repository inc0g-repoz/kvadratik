package com.github.inc0grepoz.kvad.entities;

import java.awt.Point;
import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.server.KvadratikServer;
import com.github.inc0grepoz.kvad.utils.JSON;

public class BeingFactory {

    private final BeingTemplate[] templates;
    private final String[] types;

    {
        String beingsJson = KvadratikServer.ASSETS.textFile("assets/beings/beings.json");
        templates = JSON.fromJsonBeingTemplates(beingsJson);
        types = Stream.of(templates).map(BeingTemplate::getType).toArray(String[]::new);
    }

    public String[] getTypes() {
        return types;
    }

    public Being create(String type, Level level, Point point) {
        Being being = getTemplate(type).create(level, point);
        level.getBeings().add(being);
        return being;
    }

    public Player createPlayer(Connection connection, String name,
            Level level, Point point, String type) {
        BeingTemplate temp = getTemplate(type);
        Player player = temp.createPlayer(connection, name, level, point);
        level.getBeings().add(player);
        return player;
    }

    public BeingTemplate getTemplate(String type) {
        for (int i = 0; i < templates.length; i++) {
            if (templates[i].getType().equals(type)) {
                return templates[i];
            }
        }
        return null;
    }

}