package com.github.inc0grepoz.kvad.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class AssetsManager {

    public String assetsParent;

    public BufferedImage image(String path) {
        String ppp = getAssetsParent() + path;
        Logger.info("Loading " + ppp);

        try {
            return ImageIO.read(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {}

        try {
            return ImageIO.read(new File(ppp));
        } catch (Exception e) {}

        Logger.error("Invalid image: " + ppp);
        System.exit(0);
        return null;
    }

    public String textFile(String path) {
        String ppp = getAssetsParent() + path;
        Logger.info("Loading " + ppp);

        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(ppp);
            if (stream == null) {
                File file = new File(ppp);
                stream = new FileInputStream(file);
            }
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            String string = br.lines().collect(StringBuilder::new,
                    (b, s) -> b.append(s), (b1, b2) -> b1.append(b2))
                    .toString();
            stream.close();
            isr.close();
            br.close();
            return string;
        } catch (Exception e) {}

        Logger.error("Invalid text file: " + ppp);
        System.exit(0);
        return null;
    }

    private String getAssetsParent() {
        return assetsParent == null ? "" : assetsParent + "/";
    }

}
