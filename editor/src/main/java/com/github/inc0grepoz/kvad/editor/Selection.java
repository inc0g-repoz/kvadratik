package com.github.inc0grepoz.kvad.editor;

import lombok.Getter;
import lombok.Setter;

public class Selection {

    public static enum SelectionMode {
        GRID, POINT
    }

    public final SelectionTarget selTar = new SelectionTarget();
    public final SelectionGrid selGrid;

    public Selection(KvadratikEditor editor) {
        selGrid = new SelectionGrid(editor);
    }

    private @Getter @Setter SelectionMode mode = SelectionMode.GRID;

}
