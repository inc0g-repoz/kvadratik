package com.github.inc0grepoz.kvad.editor.awt;

import java.util.stream.Stream;

import javax.swing.DropMode;
import javax.swing.JList;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;

@SuppressWarnings("serial")
public class ObjectList extends JList<String> {

    ObjectList(KvadratikEditor editor) {
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        setCellRenderer(new ObjectListCellRenderer());
        addListSelectionListener(e -> {
            editor.getSelection().selGrid.setFactory(KvadratikEditor.OBJECT_FACTORY);
        });
        updateData();
    }

    public void updateData() {
        String[] listData = Stream.of(KvadratikEditor.OBJECT_FACTORY.getTemplates())
                .map(LevelObjectTemplate::getType).toArray(String[]::new);
        setListData(listData);
    }

}
