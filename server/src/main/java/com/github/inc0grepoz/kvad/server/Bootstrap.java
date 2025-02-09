package com.github.inc0grepoz.kvad.server;

import com.github.inc0grepoz.kvad.server.server.KvadratikServer;

public class Bootstrap {

    public static void main(String[] args) {
        KvadratikServer.INSTANCE.run();
    }

}
