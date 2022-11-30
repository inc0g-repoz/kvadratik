package com.github.inc0grepoz.kvad.editor;

public class Selection {

    public static enum SelectionMode {
        GRID, POINT
    }

    public final SelectionPoint selTar = new SelectionPoint();
    public final SelectionGrid selGrid;

    public Selection(KvadratikEditor editor) {
        selGrid = new SelectionGrid(editor);
    }

    private SelectionMode selMode = SelectionMode.GRID;

    public SelectionMode getMode() {
        return selMode;
    }

    public void setMode(SelectionMode selMode) {
        if (selMode == null) {
            return;
        }
        this.selMode = selMode;
    }

}
