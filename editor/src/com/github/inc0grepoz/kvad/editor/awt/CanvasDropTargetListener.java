package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.Level;

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
        String objectType;
        try {
            objectType = (String) event.getTransferable()
                    .getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            return;
        } catch (IOException e) {
            return;
        }

        Level level = editor.getLevel();
        Point cam = level.getCamera().getRectangle().getLocation();
        Point obj = event.getLocation();
        obj.x += cam.x;
        obj.y += cam.y;

        KvadratikEditor.OBJECT_FACTORY.create(objectType, level, obj);
    }

}
