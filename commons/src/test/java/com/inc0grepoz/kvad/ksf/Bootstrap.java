package com.inc0grepoz.kvad.ksf;

import java.io.IOException;

import com.github.inc0grepoz.kvad.utils.Logger;

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




/*      REGEX TEST
        String string = "\t  one, two  , three";
        string = string.replaceAll("(^(\t| )+)|((\t| )+$)", "").replaceAll(string, string);
        String regex = "((\\t| )+)?,((\\t| )+)?| ";
        String resolved = String.join("<here>", string.split(regex));
        Logger.info(resolved);
*/



        Logger.info(Boolean.parseBoolean("true && true"));



//        ScriptTree tree = new ScriptTree("script.kcs");
//        Logger.info("Script tree\n" + tree.toString());
    }

    public static void test(Object i) {
        Logger.info(i);
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
