package com.github.inc0grepoz.kvad.entities.factory;

import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.client.KvadratikGame;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.entities.level.LevelObject;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectAnim;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.utils.JSON;

public class LevelObjectFactory {

    private LevelObjectTemplate[] templates;

    public void validatePreload() {
        if (templates == null) {
            String levelObjectsJson = KvadratikGame.ASSETS.textFile("assets/objects/objects.json");
            String levelObjectsAnimsJson = KvadratikGame.ASSETS.textFile("assets/objects/anims.json");
            LevelObjectAnim.init(JSON.fromJsonLevelObjectAnim(levelObjectsAnimsJson));
            templates = JSON.fromJsonLevelObjectTemplates(levelObjectsJson);
        }
    }

    public LevelObjectTemplate getTemplate(String type) {
        for (int i = 0; i < templates.length; i++) {
            if (templates[i].getType().equals(type)) {
                return templates[i];
            }
        }
        return null;
    }

    public LevelObject create(String name, Level level, Point point) {
        LevelObject levelObject = getTemplate(name).create(level, point);
        level.getLevelObjects().add(levelObject);
        return levelObject;
    }

}
