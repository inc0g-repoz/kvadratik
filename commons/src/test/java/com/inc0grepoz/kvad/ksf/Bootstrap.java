package com.inc0grepoz.kvad.ksf;

import java.io.IOException;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.ksf.Event;
import com.github.inc0grepoz.kvad.ksf.ScriptManager;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        Kvadratik kvad = new Tester();
        ScriptManager scriptMan = new ScriptManager(kvad);

        scriptMan.loadScripts();

        Event event = new Event("testIfElse") {

            public int[] arr = { 1, 2 };

            public int call(int a) {
                return a;
            }

            public Object self() {
                return this;
            }

        };

        bench(() -> scriptMan.fireEvent(event));
    }

    public static void bench(Runnable... rArr) {
        long a;
        for (int i = 0; i < rArr.length; i++) {
            a = System.currentTimeMillis();
            rArr[i].run();
            System.out.println((System.currentTimeMillis() - a) + " ms");
        }
    }

}

class Tester implements Kvadratik {

    AssetsProvider assets = new AssetsProvider();

    @Override
    public AssetsProvider getAssetsProvider() {
        return assets;
    }

}
