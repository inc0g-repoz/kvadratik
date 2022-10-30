package com.github.inc0grepoz;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.github.inc0grepoz.kvad.utils.AssetsManager;

@SuppressWarnings("serial")
public abstract class GameFrame extends Frame {

    private final static AssetsManager ASSETS = new AssetsManager();

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

    public static AssetsManager getAssets() {
        return ASSETS;
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

}
