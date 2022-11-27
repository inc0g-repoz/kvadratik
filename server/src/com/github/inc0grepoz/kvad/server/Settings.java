package com.github.inc0grepoz.kvad.server;

import java.util.Map;

public class Settings {

    public final String assetsLink;
    public final int port, timeout;

    public Settings(Map<String, Object> map) {
        assetsLink = (String) map.get("assetsLink");
        port = (int) map.get("port");
        timeout = (int) map.get("timeout");
    }

}
