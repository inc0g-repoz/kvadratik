package com.github.inc0grepoz;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.inc0grepoz.kvad.KvadratikGame;

public class Controls implements KeyListener {

    public static enum Key {
        CAMERA_MODE,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        SELECT_UP,
        SELECT_DOWN,
        SELECT_LEFT,
        SELECT_RIGHT,
        SPRINT,
        JUMP
    }

    private final KvadratikGame game;
    private final HashMap<Key, Integer> controls = new HashMap<>();
    private final ArrayList<Integer> pressed = new ArrayList<>();

    public Controls(KvadratikGame game) {
        this.game = game;
        defaults();
    }

    public KvadratikGame getGame() {
        return game;
    }

    public void defaults() {
        controls.put(Key.CAMERA_MODE, 86);       // V
        controls.put(Key.MOVE_UP, 87);           // W
        controls.put(Key.MOVE_LEFT, 65);         // A
        controls.put(Key.MOVE_DOWN, 83);         // S
        controls.put(Key.MOVE_RIGHT, 68);        // D
        controls.put(Key.SELECT_UP, 38);         // Up
        controls.put(Key.SELECT_DOWN, 40);       // Left
        controls.put(Key.SELECT_LEFT, 37);       // Down
        controls.put(Key.SELECT_RIGHT, 39);      // Right
        controls.put(Key.SPRINT, 16);            // Shift
        controls.put(Key.JUMP, 32);              // Jump
    }

    public boolean isPressed(Key key) {
        try {
            int code = controls.get(key);
            return pressed.contains(code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("Typed " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressed.contains(e.getKeyCode())) {
            pressed.add(e.getKeyCode());
            System.out.println("Pressed " + e.getKeyChar() + " (" + e.getKeyCode() + ")");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            if (pressed.contains(e.getKeyCode())) {
                pressed.removeIf(code -> code == e.getKeyCode());
            }
        } catch (Exception ex) {
            System.err.println("Shit, won't release cuz " + ex.getCause());
        }
        System.out.println("Released " + e.getKeyChar() + " (" + e.getKeyCode() + ")");
    }

}
