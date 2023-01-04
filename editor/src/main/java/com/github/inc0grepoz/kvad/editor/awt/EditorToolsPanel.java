package com.github.inc0grepoz.kvad.editor.awt;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

import lombok.Getter;

@SuppressWarnings("serial")
public class EditorToolsPanel extends JPanel {

    private final SelectionModesComboBox selModes;
    private final JLabel lGridSize = new JLabel("Grid size");
    private final JTextField tfGridSize = new JTextField("64", 5);
    private final JCheckBox cbAuto = new JCheckBox("Auto");
    private final @Getter ObjectList objectsList;

    private @Getter int gridSize = 64;

    public EditorToolsPanel(KvadratikEditor editor) {
        // Components are placed vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Selector modes combo-box
        JPanel cboxSelModes = new JPanel();
        selModes = new SelectionModesComboBox(editor);
        cboxSelModes.add(selModes);

        // Grid size option
        JPanel gsPanel = new JPanel();
        gsPanel.add(lGridSize);  // Grid size label
        gsPanel.add(tfGridSize); // Grid size text field
        gsPanel.add(cbAuto);     // Grid auto-size option

        // Level objects list
        objectsList = new ObjectList(editor);

        // On apply value
        tfGridSize.addActionListener(a -> {
            try {
                gridSize = Integer.valueOf(tfGridSize.getText());
            } catch (NumberFormatException e) {
                gridSize = 0;
            }
        });

        // Adding all tools panel subcomponents
        add(cboxSelModes); // Selection modes combo-box
        add(gsPanel);      // Tools panel
        add(objectsList);  // Level objects list
    }

    public boolean isAutoGridSizeEnabled() {
        return cbAuto.isSelected();
    }

}
