package com.github.inc0grepoz.kvad.entities.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;

import lombok.Getter;

@SuppressWarnings("unchecked")
public class BeingFactory {

    private final Map<String, BeingTemplate> templates = new HashMap<>();
    private @Getter String[] types;

    public void validatePreload() {
        if (types == null) {
            String json = KvadratikGame.ASSETS.textFile("assets/beings/beings.json");
            List<BeingTemplate> list = KvadratikGame.JSON_MAPPER.deserialize(json, List.class, BeingTemplate.class);
            list.forEach(bt -> templates.put(bt.getType(), bt));
            types = list.stream().map(BeingTemplate::getType).toArray(String[]::new);
        }
    }

    public BeingTemplate getTemplate(String type) {
        return templates.get(type);
    }

    public Being create(String type, Level level, Point point) {
        return getTemplate(type).create(level, point);
    }

    public Being create(String type, Level level, Point point, int id) {
        return getTemplate(type).create(level, point, id);
    }

}
