package com.github.inc0grepoz.kvad.ksf;

public enum Keyword {

    ELSE("else"),
    EVENT("on"),
    FOR("for"),
    IF("if"),
    VAR("var"),
    WHILE("while");

    final String word;

    Keyword(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

}
