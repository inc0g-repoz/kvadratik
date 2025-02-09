package com.github.inc0grepoz.kvad.common.entities.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.inc0grepoz.kvad.common.Kvadratik;
import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.common.utils.Platform;

@SuppressWarnings("unchecked")
public class LevelObjectFactory implements RenderableFactory {

    private final Map<String, LevelObjectTemplate> templates = new HashMap<>();

    public void validatePreload() {
        if (templates.isEmpty()) {
            Kvadratik kvad = Objects.requireNonNull(Platform.getInstance(), "null platform");

            // Preloading level objects animations
            String levelObjectsAnimsJson = kvad.getAssetsProvider().textFile("assets/objects/anims.json");
            List<LevelObjectAnim> animList = kvad.getJsonMapper().deserialize(levelObjectsAnimsJson, List.class, LevelObjectAnim.class);
            Map<String, LevelObjectAnim> animMap = new HashMap<>();
            animList.forEach(anim -> {
                anim.precacheSprites();
                animMap.put(anim.getName(), anim);
            });
            LevelObjectAnim.init(animMap);

            // Preloading level objects data
            String levelObjectsJson = kvad.getAssetsProvider().textFile("assets/objects/objects.json");
            List<LevelObjectTemplate> list = kvad.getJsonMapper().deserialize(levelObjectsJson, List.class, LevelObjectTemplate.class);
            list.forEach(lot -> templates.put(lot.getName(), lot));
        }
    }

    @Override
    public Collection<LevelObjectTemplate> getTemplates() {
        return templates.values();
    }

    @Override
    public LevelObjectTemplate getTemplate(String type) {
        return templates.get(type);
    }

    @Override
    public LevelObject create(String name, Level level, Point point) {
        LevelObject levelObject = getTemplate(name).create(level, point);
        level.getLevelObjects().add(levelObject);
        return levelObject;
    }

}
