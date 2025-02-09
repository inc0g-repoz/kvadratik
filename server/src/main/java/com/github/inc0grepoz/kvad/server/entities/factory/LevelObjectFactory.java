package com.github.inc0grepoz.kvad.server.entities.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.inc0grepoz.kvad.common.Kvadratik;
import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.common.utils.Platform;

@SuppressWarnings("unchecked")
public class LevelObjectFactory {

    private final Map<String, LevelObjectTemplate> templates = new HashMap<>();

    public void validatePreload() {
        if (templates.isEmpty()) {
            Kvadratik kvad = Platform.getInstance();

            // Preloading level objects data
            String levelObjectsJson = kvad.getAssetsProvider().textFile("assets/objects/objects.json");
            List<LevelObjectTemplate> list = kvad.getJsonMapper().deserialize(levelObjectsJson, List.class, LevelObjectTemplate.class);
            list.forEach(lot -> templates.put(lot.getType(), lot));
        }
    }

    public LevelObjectTemplate getTemplate(String type) {
        return templates.get(type);
    }

    public LevelObject create(String name, Level level, Point point) {
        LevelObject levelObject = getTemplate(name).create(level, point);
        level.getLevelObjects().add(levelObject);
        return levelObject;
    }

}
