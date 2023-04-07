package com.github.inc0grepoz.kvad.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class AssetsProvider {

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

        InputStream stream;
        File file = new File(ppp);

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

        Logger.error("Invalid text file: " + ppp);
        System.exit(0);
        return null;
    }

    public Stream<String> listFiles(String path) {
        String ppp = getAssetsParent() + path;
        try {
            File dir = new File(ppp);
            return Stream.of(dir.list()).map(fn -> ppp + "/" + fn);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            List<String> files = new ArrayList<>();
            ZipUtil.listFiles(ppp).stream()
                    .filter(name -> !name.endsWith("/") && !name.endsWith("\\"))
                    .forEach(filePath -> {
                files.add(filePath);
            });
            return files.stream();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Logger.error("Invalid directory: " + ppp);
        System.exit(0);
        return null;
    }

    private String getAssetsParent() {
        return assetsParent == null ? "" : assetsParent + "/";
    }

}
