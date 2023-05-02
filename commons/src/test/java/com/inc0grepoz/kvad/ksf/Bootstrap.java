package com.inc0grepoz.kvad.ksf;

import java.io.IOException;

import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.ksf.ScriptManager;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
/*      BENCHMARKING
        bench(
        () -> {
            System.out.println("for (int i = 0; i < 0; i++)".matches("(for ?)\\((.+ .+ ?= ?.+);(.+);(.+)\\)"));
        },
        () -> {
            System.out.println("for (int i = 0; i < 0; i++)".matches("(for ?)\\((.+ .+ ?= ?.+);(.+);(.+)\\)"));
        },
        () -> {
            System.out.println("for (int i = 0; i < 0; i++)".matches("(for ?)\\((.+ .+ ?= ?.+);(.+);(.+)\\)"));
        }
        );
        */



        Kvadratik kvad = new Tester();
        ScriptManager scriptMan = new ScriptManager(kvad);
        scriptMan.loadScripts();

        Object event = new Object() {
            public void call(int a) {
                System.out.println(a);
            }
        };
        scriptMan.fireEvent("test", event);
    }

    public static void bench(Runnable... rArr) {
        long a;
        for (int i = 0; i < rArr.length; i++) {
            a = System.currentTimeMillis();
            rArr[i].run();
            System.out.println(System.currentTimeMillis() - a);
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
