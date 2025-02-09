package com.github.inc0grepoz.kvad.editor;

import com.github.inc0grepoz.kvad.common.entities.Renderable;

import lombok.Getter;

public class SelectionTarget {

    private @Getter Renderable target;

    public void setTarget(Renderable target) {
        clearSelection();
        if (target != null) {
            this.target = target;
            target.selected = true;
        }
    }

    public void clearSelection() {
        if (target != null) {
            target.selected = false;
            target = null;
        }
    }

}
