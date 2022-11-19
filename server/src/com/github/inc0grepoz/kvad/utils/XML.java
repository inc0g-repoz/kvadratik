package com.github.inc0grepoz.kvad.utils;

public class XML extends XMLSection {

    public static XML fromString(String string) {
        return new XML(string);
    }

    private final String xmlString;

    protected XML(String xmlString) {
        super(deserialize(xmlString));
        this.xmlString = xmlString;
    }

    @Override
    public String toString() {
        return xmlString;
    }

}
