package com.github.inc0grepoz;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Controls implements KeyListener {

    private final HashMap<Key, Integer> controls = new HashMap<>();
    private final ArrayList<Integer> pressed = new ArrayList<>();

    {
        defaults();
    }

    public void defaults() {
        controls.put(Key.MOVE_FORWARD, 87);    // W
        controls.put(Key.MOVE_LEFT, 65);       // A
        controls.put(Key.MOVE_BACK, 83);       // S
        controls.put(Key.MOVE_RIGHT, 68);      // D
        controls.put(Key.SPRINT, 16);          // Shift
        controls.put(Key.JUMP, 32);            // Jump
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
            System.out.println("Pressed " + e.getKeyChar());
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
        System.out.println("Released " + e.getKeyChar());
    }

}
