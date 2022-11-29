package com.github.inc0grepoz.kvad.editor;

import com.github.inc0grepoz.kvad.entities.Renderable;

public class Selection {

    private Renderable renEnt;

    public Renderable getTarget() {
        return renEnt;
    }

    public void setTarget(Renderable renEnt) {
        clearSelection();
        this.renEnt = renEnt;
        renEnt.selected = true;
    }

    public void clearSelection() {
        if (renEnt != null) {
            renEnt.selected = false;
            renEnt = null;
        }
    }

}
