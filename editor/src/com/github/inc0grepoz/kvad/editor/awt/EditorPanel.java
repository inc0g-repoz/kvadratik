package com.github.inc0grepoz.kvad.editor.awt;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.inc0grepoz.kvad.editor.KvadratikEditor;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

    public final ObjectList jlObjects = new ObjectList();

    public EditorPanel(KvadratikEditor editor) {
        // Components are placed vertically
//      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//      setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Selector modes
        JPanel cboxSelModes = new JPanel();
        SelectionModesComboBox selModes = new SelectionModesComboBox(editor);
        cboxSelModes.add(selModes);

        // Grid size option
        JPanel gsPanel = new JPanel();
        JLabel lGridSize = new JLabel("Grid size");
        JTextField tfGridSize = new JTextField("64", 5);
        gsPanel.add(lGridSize);
        gsPanel.add(tfGridSize);

        // Grid size option
        JCheckBox cbAuto = new JCheckBox("Auto");

        add(cboxSelModes);
        add(gsPanel);
        add(cbAuto);

        // Adding all tools panel subcomponents
        add(jlObjects); // Level objects list
    }

}
