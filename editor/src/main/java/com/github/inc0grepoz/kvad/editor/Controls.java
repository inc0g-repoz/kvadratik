package com.github.inc0grepoz.kvad.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lombok.Getter;

public class Controls implements KeyListener {

    public static enum Key {

        BACKSPACE    (8),   // Backspace
        CAMERA_MODE  (86),  // V
        CHAT         (84),  // T
        ENTER        (10),  // Enter
        ESCAPE       (27),  // Escape
        MOVE_UP      (87),  // W
        MOVE_DOWN    (83),  // S
        MOVE_LEFT    (65),  // A
        MOVE_RIGHT   (68),  // D
        SELECT_UP    (38),  // Up
        SELECT_DOWN  (40),  // Down
        SELECT_LEFT  (37),  // Left
        SELECT_RIGHT (39),  // Right
        SPRINT       (16),  // Shift
        JUMP         (32),  // Space
        DELETE       (127); // Delete

        private static Key byCode(int code) {
            Key[] values = values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].code == code) {
                    return values[i];
                }
            }
            return null;
        }

        boolean pressed;
        int code;

        Key(int code) {
            this.code = code;
        }

    }

    public static boolean isCameraMovingAlt() {
        return Key.MOVE_UP.pressed || Key.MOVE_DOWN.pressed
                || Key.MOVE_LEFT.pressed || Key.MOVE_RIGHT.pressed;
    }

    public static boolean isCameraMoving() {
        return Key.SELECT_UP.pressed || Key.SELECT_DOWN.pressed
                || Key.SELECT_LEFT.pressed || Key.SELECT_RIGHT.pressed;
    }

    private final @Getter KvadratikEditor editor;
    private final ControlsHandler handler;

    public Controls(KvadratikEditor editor) {
        this.editor = editor;
        handler = new ControlsHandler(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Key key = Key.byCode(e.getKeyCode());
        if (key != null && !key.pressed) {
            key.pressed = true;
            handler.onKeyPressed(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Key key = Key.byCode(e.getKeyCode());
        if (key != null && key.pressed) {
            key.pressed = false;
            handler.onKeyReleased(key);
        }
    }

}
