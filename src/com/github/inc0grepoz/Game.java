package com.github.inc0grepoz;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.inc0grepoz.kvad.entities.level.Level;

@SuppressWarnings("serial")
public abstract class Game extends Frame {

    private Level level = new Level(this);

    {
        WindowAdapter adapter = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }

        };
        addWindowListener(adapter);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            adapter.windowClosed(null);
        }));
    }

    public void applyIcon(String fileName) {
        try {
            setIconImage(ImageIO.read(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Level getLevel() {
        return level;
    }

}
