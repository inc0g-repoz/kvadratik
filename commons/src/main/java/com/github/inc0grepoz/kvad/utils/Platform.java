package com.github.inc0grepoz.kvad.utils;

import com.github.inc0grepoz.kvad.Kvadratik;

public class Platform {

    private static Kvadratik instance;

    public static <T extends Kvadratik> T init(T instance) {
        if (Platform.instance != null) {
            throw new IllegalStateException("Platform already has been initialized");
        }

        Logger.info("Intitializing the platform");
        Platform.instance = instance;

        return instance;
    }

    public static Kvadratik getInstance() {
        return instance;
    }

}
