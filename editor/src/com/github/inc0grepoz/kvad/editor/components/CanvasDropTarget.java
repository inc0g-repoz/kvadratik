package com.github.inc0grepoz.kvad.editor.components;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.util.TooManyListenersException;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.utils.Logger;

@SuppressWarnings("serial")
public class CanvasDropTarget extends DropTarget {

    public CanvasDropTarget(KvadratikEditor editor) {
        CanvasDropTargetListener listener = new CanvasDropTargetListener(editor);
        addDropTargetListener(listener);
//      canvas.getDropTarget().setActive(true);
    }

    @Override
    public void addDropTargetListener(DropTargetListener listener) {
        try {
            super.addDropTargetListener(listener);
        } catch (TooManyListenersException e) {
            Logger.error("Too many drop target listeners");
        }
    }

}
