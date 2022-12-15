package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.entities.level.LevelObjectTemplate;

@SuppressWarnings("serial")
public class ObjectListCellRenderer extends DefaultListCellRenderer {

    private JLabel label;

    ObjectListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

        String tempStr = (String) value;
        LevelObjectTemplate temp = KvadratikEditor.OBJECT_FACTORY.getTemplate(tempStr);
        label.setIcon(temp.getListIcon());
        label.setText(temp.getType());
        label.setToolTipText("Drag me!");

        if (selected) {
            label.setBackground(Color.LIGHT_GRAY);
            label.setForeground(Color.BLACK);
        } else {
            label.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
        }

        return label;
    }
}
