package com.github.inc0grepoz.kvad.editor.awt;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.github.inc0grepoz.kvad.entities.Renderable;
import com.github.inc0grepoz.kvad.utils.Logger;

@SuppressWarnings("serial")
public class PopupRenent extends JPopupMenu {

    private final JMenuItem miSetType = new JMenuItem("Set Type");
    private final JMenuItem miDelete  = new JMenuItem("Delete");

    Renderable renEnt;

    {
        miSetType.addActionListener(e -> {
            Logger.info("Set Type selected");
        });
        add(miSetType);

        miDelete.addActionListener(e -> {
            if (renEnt != null) {
                renEnt.delete();
                renEnt = null;
            }
        });
        add(miDelete);
    }

}
