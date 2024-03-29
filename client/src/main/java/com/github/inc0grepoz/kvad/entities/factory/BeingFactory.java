package com.github.inc0grepoz.kvad.entities.factory;

import java.util.stream.Stream;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.JSON;
import lombok.Getter;

public class BeingFactory {

    private BeingTemplate[] templates;
    private @Getter String[] types;

    public void validatePreload() {
        if (templates == null && types == null) {
            String beingsJson = KvadratikGame.ASSETS.textFile("assets/beings/beings.json");
            templates = JSON.fromJsonBeingTemplates(beingsJson);
            types = Stream.of(templates).map(BeingTemplate::getType).toArray(String[]::new);
        }
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

    public Being create(String type, Level level, Point point, int id) {
        return getTemplate(type).create(level, point, id);
    }

}
