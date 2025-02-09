package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import com.github.inc0grepoz.kvad.common.awt.geom.Point;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

public class CanvasDropTargetListener implements DropTargetListener {

    private final KvadratikEditor editor;

    public CanvasDropTargetListener(KvadratikEditor editor) {
        this.editor = editor;
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {}

    @Override
    public void dragOver(DropTargetDragEvent event) {}

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
        // What's this?
    }

    @Override
    public void dragExit(DropTargetEvent event) {}

    @Override
    public void drop(DropTargetDropEvent event) {
        String objectType = editor.getPanel().getObjectsList().getSelectedValue();
        if (objectType == null) {
            return;
        }

        Level level = editor.getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point obj = Point.fromAwtPoint(event.getLocation());
        obj.x += cam.x;
        obj.y += cam.y;

        KvadratikEditor.OBJECT_FACTORY.create(objectType, level, obj);
    }

}
