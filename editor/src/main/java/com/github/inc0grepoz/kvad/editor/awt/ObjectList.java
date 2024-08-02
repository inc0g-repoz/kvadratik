package com.github.inc0grepoz.kvad.editor.awt;

import javax.swing.DropMode;
import javax.swing.JList;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;

@SuppressWarnings("serial")
public class ObjectList extends JList<String> {

    private final KvadratikEditor editor;

    ObjectList(KvadratikEditor editor) {
        this.editor = editor;

        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        setCellRenderer(new ObjectListCellRenderer());
        addListSelectionListener(e -> {
            editor.getSelection().selGrid.setFactory(KvadratikEditor.OBJECT_FACTORY);
        });

        updateData();
    }

    public void updateData() {
        String[] listData = editor.getLevelObjectFactory().getTemplates().stream()
                .map(LevelObjectTemplate::getName).toArray(String[]::new);
        setListData(listData);
    }

}
