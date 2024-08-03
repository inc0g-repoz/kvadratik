package com.github.inc0grepoz.kvad.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private String assetsParent;

    // Makes it run in your IDE
    {
        String parent = "src/main/resources";
        File file = new File(parent);
        assetsParent = file.exists() ? parent + "/" : "";
    }

    public boolean copy(String path) {
        String ppp = assetsParent + path;
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
        String ppp = assetsParent + path;
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
        String ppp = assetsParent + path;
        Logger.info("Loading " + ppp);

        File file = new File(ppp);
        try {
            if (file.exists()) {
                return textFile(file);
            }

            return textFile(getClass().getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {}

        Logger.error("Invalid text file: " + ppp);
        System.exit(0);
        return null;
    }

    public String textFile(File file) {
        try {
            return textFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {}
        System.exit(0);
        return null;
    }

    public String textFile(InputStream stream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            return br.lines().collect(StringBuilder::new, (b, s) -> b.append(s), (b1, b2) -> b1.append(b2)).toString();
        } catch (IOException e) {}
        System.exit(0);
        return null;
    }

    public Script script(String path, VarPool vars) {
        String ppp = assetsParent + path;

        Script script = Script.compile(new File(ppp), vars);
        Logger.info("Compiled and loaded " + script.getName());

        return script;
    }

    public List<Script> scripts(String path, VarPool vars) {
        String ppp = assetsParent + "scripts";
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
        String ppp = assetsParent + path;
        try {
            File dir = new File(ppp);
            return Stream.of(dir.list()).map(fn -> path + "/" + fn);
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

    public void setAssetsParent(String path) {
        Logger.info("Set assets parent directory to " + path);
        assetsParent = path == null ? "" : (path + "/").replaceAll("/+", "/");
    }

}
