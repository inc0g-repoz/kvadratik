package com.github.inc0grepoz;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Assets {

    public BufferedImage image(String path) {
        try {
            return ImageIO.read(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {}

        try {
            return ImageIO.read(new File("src/" + path));
        } catch (Exception e) {}

        System.out.println("Invalid image: " + path);
        System.exit(0);
        return null;
    }

}
