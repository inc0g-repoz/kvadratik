package com.github.inc0grepoz.kvad.server;

import java.util.Map;

public class Settings {

    public final String assetsLink;
    public final int timeout;

    public Settings(Map<String, Object> map) {
        assetsLink = (String) map.get("assetsLink");
        timeout = (int) map.get("timeout");
    }

}
