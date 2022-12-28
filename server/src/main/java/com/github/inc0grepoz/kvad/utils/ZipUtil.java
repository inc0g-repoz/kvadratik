package com.github.inc0grepoz.kvad.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static String[] listClasses(String packageName) {
        try {
            return listFiles(packageName.replace(".", "/")).stream()
                    .filter(name -> name.endsWith(".class"))
                    .map(name -> name.substring(0, name.length() - 6))
                    .toArray(String[]::new);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return new String[0];
    }

    public static ArrayList<String> listFiles(String path) throws Throwable {
        String src = ZipUtil.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        if (!src.endsWith(".jar")) {
            return null;
        }

        File file = new File(src);
        ZipFile zip = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zip.entries();

        ArrayList<String> fileEntries = new ArrayList<>();
        for (ZipEntry entry = null; entries.hasMoreElements()
                && (entry = entries.nextElement()) != null;) {
            if (entry.getName().startsWith(path)) {
                fileEntries.add(entry.getName());
            }
        }

        zip.close();
        return fileEntries;
    }

}
