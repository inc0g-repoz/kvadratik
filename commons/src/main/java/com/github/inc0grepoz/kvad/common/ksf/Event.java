package com.github.inc0grepoz.kvad.common.ksf;

import java.awt.Color;

import com.github.inc0grepoz.kvad.common.utils.Logger;

import lombok.Getter;

public class Event {

    public static Logger logger = new Logger();
    public static Color color = Color.WHITE;

    private final @Getter String name;

    public Event(String name) {
        this.name = name;
    }

}
