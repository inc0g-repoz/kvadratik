package com.inc0grepoz.kvad.ksf;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        /*
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
/*
        Logger.info("for (i = 0; i < 1; i++)".matches(

            "((\t| )+)?for ?\\((.+)=(.+);(.+);(.+)\\)"

        ));
*/

//        ScriptTree tree = new ScriptTree("script.kcs");
//        Logger.info("Script tree\n" + tree.toString());
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
