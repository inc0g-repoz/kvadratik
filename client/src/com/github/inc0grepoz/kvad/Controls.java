package com.github.inc0grepoz.kvad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener {

    public static enum Key {

        CAMERA_MODE  (86),
        MOVE_UP      (87), // V
        MOVE_DOWN    (83), // S
        MOVE_LEFT    (65), // A
        MOVE_RIGHT   (68), // D
        SELECT_UP    (38), // Up
        SELECT_DOWN  (40), // Down
        SELECT_LEFT  (37), // Left
        SELECT_RIGHT (39), // Right
        SPRINT       (16), // Shift
        JUMP         (32), // Space
        NAN          (-1);

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

    private final KvadratikGame game;
    private final ControlsHandler handler;

    public Controls(KvadratikGame game) {
        this.game = game;
        handler = new ControlsHandler(this);
    }

    public KvadratikGame getGame() {
        return game;
    }

    public boolean isPressed(Key key) {
        return key.pressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("Typed " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Key key = Key.byCode(e.getKeyCode());
        if (!key.pressed) {
            key.pressed = true;
            handler.onKeyPressed(key);

            if (game.getConsole().isLoggingKeys()) {
                System.out.println("Pressed " + e.getKeyChar() + " (" + e.getKeyCode() + ")");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Key key = Key.byCode(e.getKeyCode());
        if (key.pressed) {
            key.pressed = false;
            handler.onKeyReleased(key);

            if (game.getConsole().isLoggingKeys()) {
                System.out.println("Released " + e.getKeyChar() + " (" + e.getKeyCode() + ")");
            }
        }
    }

}
