package com.github.inc0grepoz.kvad.ksf;

public enum Keyword {

    EVENT("on", true),
    FOR("for", true),
    IF("if", true),
    VAR("var", false),
    WHILE("while", true);

    final String word;
    final boolean scope;

    Keyword(String word, boolean scope) {
        this.word = word;
        this.scope = scope;
    }

    public String toString() {
        return word;
    }

}
