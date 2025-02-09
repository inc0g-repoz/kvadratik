package com.github.inc0grepoz.kvad.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Downloader {

    public static void download(URL url, File file) {
        try {
            Path dest = file.toPath();
            try (InputStream inputStream = url.openStream()) {
                file.mkdirs();
                file.createNewFile();
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

}
