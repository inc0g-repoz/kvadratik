package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.stream.Stream;

import javax.swing.JComboBox;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.editor.Selection.SelectionMode;

@SuppressWarnings("serial")
public class SelectionModesComboBox extends JComboBox<String> {

    public SelectionModesComboBox(KvadratikEditor editor) {
        super(Stream.of(SelectionMode.values())
                .map(SelectionMode::name)
                .toArray(String[]::new));
        ItemListener il = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                editor.selection.setMode(SelectionMode.valueOf(item));
            }
        };
        addItemListener(il);
    }

}
