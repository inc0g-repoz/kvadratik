package com.inc0grepoz.kvad.ksf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.inc0grepoz.commons.util.json.mapper.JsonMapper;
import com.github.inc0grepoz.kvad.Kvadratik;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.ksf.Event;
import com.github.inc0grepoz.kvad.ksf.ScriptManager;
import com.github.inc0grepoz.kvad.utils.AssetsProvider;

import lombok.Getter;

@SuppressWarnings("all") // It's all used
public class Bootstrap {

    public static void main(String[] args) throws IOException {
        Kvadratik kvad = new Kvadratik() {

            @Getter AssetsProvider assetsProvider = new AssetsProvider();
            @Getter JsonMapper jsonMapper = new JsonMapper();
            @Getter BeingFactory beingFactory;
            @Getter LevelObjectFactory levelObjectFactory;

        };

        ScriptManager scriptMan = new ScriptManager(kvad);
        scriptMan.loadScripts();

        testForEach(scriptMan);
        scriptMan.fireEvent(new Event("testFibonacci"));
    }

    public static void bench(Runnable... lambda) {
        long a;
        for (int i = 0; i < lambda.length; i++) {
            a = System.currentTimeMillis();
            lambda[i].run();
            System.out.println((System.currentTimeMillis() - a) + " ms");
        }
    }

    public static void testForEach(ScriptManager scriptMan) {
        Event event = new Event("testForEach") {

            public int[] arr = { 1, 2, 3 };

            public List<Integer> list = new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
            }};

            public int call(int a) {
                return a;
            }

            public Object self() {
                return this;
            }

        };

        bench(() -> scriptMan.fireEvent(event));Arrays.copyOfRange(new int[2], 0, 0);
    }

}
