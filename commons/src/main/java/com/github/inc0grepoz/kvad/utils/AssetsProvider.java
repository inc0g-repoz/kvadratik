package com.github.inc0grepoz.kvad.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.github.inc0grepoz.kvad.ksf.Script;
import com.github.inc0grepoz.kvad.ksf.VarPool;

public class AssetsProvider {

    public String assetsParent;

    public boolean copy(String path) {
        String ppp = getAssetsParent() + path;
        File file = new File(ppp);

        if (file.exists()) {
            return false;
        }

        Logger.info("Copying " + ppp);
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            Files.copy(is, file.toPath());
            is.close();
            return true;
        } catch (Exception e) {}

        Logger.error("Invalid file: " + ppp);
        return false;
    }

    public void playSound(String resourcePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass()
                    .getClassLoader().getResource(resourcePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
        }
    }

    public BufferedImage image(String path) {
        String ppp = getAssetsParent() + path;
        Logger.info("Loading " + ppp);

        try {
            return ImageIO.read(new File(ppp));
        } catch (Exception e) {}

        try {
            return ImageIO.read(getClass().getClassLoader().getResource(path));
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

    public List<Script> scripts(String path, VarPool vars) {
        String ppp = getAssetsParent() + "scripts";
        List<Script> scripts = new ArrayList<>();

        File dir = new File(ppp);
        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        File[] ksfArr = dir.listFiles(f -> f.isFile()
                && f.getName().toLowerCase().endsWith(Script.EXTENSION));
        Logger.info("Found " + ksfArr.length + " script(s)");

        for (File ksf : ksfArr) {
            Script script = Script.compile(ksf, vars);
            scripts.add(script);
            Logger.info("Compiled and loaded " + script.getName());
        }

        return scripts;
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
        return (assetsParent == null ? "" : assetsParent + "/").replaceAll("(/)+", "/");
    }

}
