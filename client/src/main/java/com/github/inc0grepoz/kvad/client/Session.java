package com.github.inc0grepoz.kvad.client;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;

import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.Message;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.utils.Logger;

import lombok.Getter;

public class Session {

    public static Session loadLevel(Level level) {
        return new Session(level);
    }

    public static Session loadLevel(String path) {
        String levelJson = KvadratikGame.ASSETS.textFile(path);
        return loadLevel(JSON.fromJsonLevel(levelJson, false));
    }

    public static void joinServer() {
        KvadratikClient client = KvadratikGame.INSTANCE.getClient();
        if (client.isInfoProvided()) {
            try {
                client.start();
                client.connect();
            } catch (UnknownHostException e1) {
                Logger.error("Unknown host");

                Message message = new Message();
                message.addComponent("Unknown host.", Color.RED);
                client.getChat().print(message);
            } catch (IOException e1) {
                Logger.error("Unable to join the server");

                Message message = new Message();
                message.addComponent("Unable to join the server.", Color.RED);
                client.getChat().print(message);
            }
        } else {
            Logger.info("Nothing provided for the client");
        }
    }

    private final @Getter Physics physics = new Physics(this);
    private final @Getter Level level;

    private Session(Level level) {
        this.level = level;
    }

}
