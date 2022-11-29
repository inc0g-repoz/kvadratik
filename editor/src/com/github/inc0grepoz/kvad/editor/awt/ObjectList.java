package com.github.inc0grepoz.kvad.editor.awt;

import java.util.stream.Stream;

import javax.swing.DropMode;
import javax.swing.JList;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;

@SuppressWarnings("serial")
public class ObjectList extends JList<String> {

    {
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        updateData();
    }

    public void updateData() {
        String[] listData = Stream.of(KvadratikEditor.OBJECT_FACTORY.getTemplates())
                .map(LevelObjectTemplate::getName).toArray(String[]::new);
        setListData(listData);
    }

}
