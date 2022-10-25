package com.github.inc0grepoz;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.github.inc0grepoz.kvad.KvadratikGame;

public class Bootstrap {

    public static void main(String[] args) {
        Game game = new KvadratikGame();
        game.applyIcon("icon.png");
        game.setTitle("kvadratik");
        game.setSize(640, 480);
        game.setResizable(false);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

    public static BufferedImage readImageFromAssets(String path) {
        try {
            return ImageIO.read(Bootstrap.class.getClassLoader()
                    .getResourceAsStream(path));
        } catch (Exception e) {}

        try {
            return ImageIO.read(new File("src/" + path));
        } catch (Exception e) {}

        System.out.println("Invalid image path: " + path);
        System.exit(0);
        return null;
    }

}
