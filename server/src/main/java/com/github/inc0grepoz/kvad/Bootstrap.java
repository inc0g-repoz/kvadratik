package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.server.KvadratikServer;

public class Bootstrap {

    public static void main(String[] args) {
        KvadratikServer.INSTANCE.run();
    }

}
