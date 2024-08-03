package com.github.inc0grepoz.kvad.entities.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.awt.geom.Point;
import com.github.inc0grepoz.kvad.entities.Connection;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.being.BeingTemplate;
import com.github.inc0grepoz.kvad.entities.being.Player;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.Platform;

import lombok.Getter;

@SuppressWarnings("unchecked")
public class BeingFactory {

    private final Map<String, BeingTemplate> templates = new HashMap<>();
    private @Getter String[] types;

    public void validatePreload() {
        if (types == null) {
            Kvadratik kvad = Platform.getInstance();
            String json = kvad.getAssetsProvider().textFile("assets/beings/beings.json");
            List<BeingTemplate> list = kvad.getJsonMapper().deserialize(json, List.class, BeingTemplate.class);
            list.forEach(bt -> templates.put(bt.getType(), bt));
            types = list.stream().map(BeingTemplate::getType).toArray(String[]::new);
        }
    }

    public boolean hasType(String type) {
        return templates.containsKey(type);
    }

    public BeingTemplate getTemplate(String type) {
        return templates.get(type);
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
