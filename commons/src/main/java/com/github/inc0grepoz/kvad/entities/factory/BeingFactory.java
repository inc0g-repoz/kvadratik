package com.github.inc0grepoz.kvad.entities.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Platform;

import lombok.Getter;

@SuppressWarnings("unchecked")
public class BeingFactory implements RenderableFactory {

    private final Map<String, BeingTemplate> templates = new HashMap<>();
    private @Getter String[] types;

    public void validatePreload() {
        if (types == null) {
            Kvadratik kvad = Platform.getInstance();
            String json = kvad.getAssetsProvider().textFile("assets/beings/beings.json");
            List<BeingTemplate> list = kvad.getJsonMapper().deserialize(json, List.class, BeingTemplate.class);
            list.forEach(bt -> templates.put(bt.getName(), bt));
            types = list.stream().map(BeingTemplate::getName).toArray(String[]::new);
        }
    }

    @Override
    public Collection<BeingTemplate> getTemplates() {
        return templates.values();
    }

    @Override
    public BeingTemplate getTemplate(String type) {
        return templates.get(type);
    }

    @Override
    public Being create(String type, Level level, Point point) {
        return getTemplate(type).create(level, point);
    }

    public Being create(String type, Level level, Point point, int id) {
        return getTemplate(type).create(level, point, id);
    }

}
