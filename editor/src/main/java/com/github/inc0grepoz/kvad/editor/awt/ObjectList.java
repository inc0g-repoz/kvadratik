package com.github.inc0grepoz.kvad.editor.awt;

import javax.swing.DropMode;
import javax.swing.JList;

import com.github.inc0grepoz.kvad.common.entities.level.LevelObjectTemplate;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

@SuppressWarnings("serial")
public class ObjectList extends JList<String> {

    private final KvadratikEditor editor;

    ObjectList(KvadratikEditor editor) {
        this.editor = editor;

        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        setCellRenderer(new ObjectListCellRenderer());
        addListSelectionListener(e -> {
            editor.getSelection().selGrid.setFactory(editor.getLevelObjectFactory());
        });

        updateData();
        (new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }

            updateData();
            System.out.println("leva edita");
        })).start();
    }

    public void updateData() {
        String[] listData = editor.getLevelObjectFactory().getTemplates().stream()
                .map(LevelObjectTemplate::getName).toArray(String[]::new);
        setListData(listData);
    }

}
