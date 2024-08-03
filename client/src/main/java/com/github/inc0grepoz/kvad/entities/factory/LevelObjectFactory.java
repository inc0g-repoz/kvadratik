package com.github.inc0grepoz.kvad.entities.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;

@SuppressWarnings("unchecked")
public class LevelObjectFactory {

    private final Map<String, LevelObjectTemplate> templates = new HashMap<>();

    public void validatePreload() {
        if (templates.isEmpty()) {
            // Preloading level objects animations
            String levelObjectsAnimsJson = KvadratikGame.ASSETS.textFile("assets/objects/anims.json");
            List<LevelObjectAnim> animList = KvadratikGame.JSON_MAPPER.deserialize(levelObjectsAnimsJson, List.class, LevelObjectAnim.class);
            Map<String, LevelObjectAnim> animMap = new HashMap<>();
            animList.forEach(anim -> {
                anim.precacheSprites();
                animMap.put(anim.getName(), anim);
            });
            LevelObjectAnim.init(animMap);

            // Preloading level objects data
            String levelObjectsJson = KvadratikGame.ASSETS.textFile("assets/objects/objects.json");
            List<LevelObjectTemplate> list = KvadratikGame.JSON_MAPPER.deserialize(levelObjectsJson, List.class, LevelObjectTemplate.class);
            list.forEach(lot -> templates.put(lot.getName(), lot));
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
