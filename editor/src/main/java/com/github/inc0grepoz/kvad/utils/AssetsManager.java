package com.github.inc0grepoz.kvad.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.Level;

public class AssetsManager {

    public String assetsParent;

    public BufferedImage image(String path) {
        Logger.info("Loading " + path);

        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {}

        try {
            return ImageIO.read(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {}

        Logger.error("Invalid image: " + path);
        System.exit(0);
        return null;
    }

    public String textFile(String path) {
        Logger.info("Loading " + path);

        InputStream stream;
        File file = new File(path);

        try {
            if (file.exists()) {
                stream = new FileInputStream(file);
            } else {
                stream = getClass().getClassLoader().getResourceAsStream(path);
            }

            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);

            // Reading the lines
            String string = br.lines().collect(StringBuilder::new,
                    (b, s) -> b.append(s), (b1, b2) -> b1.append(b2))
                    .toString();

            // Closing the streams
            stream.close();
            isr.close();
            br.close();

            return string;
        } catch (Exception e) {}

        Logger.error("Invalid text file: " + path);
        System.exit(0);
        return null;
    }

    public Level level(KvadratikEditor editor, String path) {
        String levelJson = textFile(path);
        Level level =  JSON.fromJsonLevel(editor, levelJson, false);
        level.setPath(path);
        return level;
    }

}
