package com.github.inc0grepoz.kvad;

import com.github.inc0grepoz.kvad.Controls.Key;

public class ControlsHandler {

    private final Controls controls;

    public ControlsHandler(Controls controls) {
        this.controls = controls;
    }

    public void onKeyPressed(Key key) {
        switch (key) {
            case SPRINT:
                controls.getGame().getLevel().getPlayer().sprint = true;
                break;
            default:
        }
    }

    public void onKeyReleased(Key key) {
        switch (key) {
            case SPRINT:
                controls.getGame().getLevel().getPlayer().sprint = false;
                break;
            default:
        }
    }

}
