package com.github.inc0grepoz.kvad.utils;

public class XML extends XMLSection {

    public static XML fromString(String string) {
        return new XML(string);
    }

    protected XML(String xml) {
        super(deserialize(xml));
    }

}
