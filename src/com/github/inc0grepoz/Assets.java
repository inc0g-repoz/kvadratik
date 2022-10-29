package com.github.inc0grepoz;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.github.inc0grepoz.kvad.utils.XML;

public class Assets {

    public BufferedImage image(String path) {
        System.out.println("Loading " + path);

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

    public String textFile(String path) {
        System.out.println("Loading " + path);
        InputStream stream;

        try {
            stream = getClass().getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                File file = new File("src/" + path);
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

        System.out.println("Invalid image: " + path);
        System.exit(0);
        return null;
    }

    public XML readXml(String path) {
        return XML.fromString(textFile(path));
    }

}
