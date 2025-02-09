package com.github.inc0grepoz.kvad.editor.awt;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;

import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.Logger;
import com.github.inc0grepoz.kvad.editor.KvadratikEditor;
import com.github.inc0grepoz.kvad.utils.JSON;

@SuppressWarnings("serial")
public class EditorToolBar extends JToolBar {

    public EditorToolBar(KvadratikEditor editor) {
        setName("Toolbar");
        setFloatable(false);

        JButton openButton = createButton("Open", "open.png", e -> {
            JFileChooser chooser = new JFileChooser();
            try {
                File srcDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                File defDir = new File(srcDir, "assets/levels");
                chooser.setCurrentDirectory(defDir.isDirectory() ? defDir : srcDir);
            } catch (URISyntaxException e1) {
            }
            chooser.showOpenDialog(null);

            File file = chooser.getSelectedFile();
            if (file != null) {
                editor.loadLevel(file);
            }
        });
        add(openButton);

        JButton saveButton = createButton("Save", "save.png", e -> {
            Level level = editor.getLevel();
            String jLevel = JSON.toJsonLevel(level);

            File fLevel = new File(level.getPath());
            if (fLevel.exists()) {
                fLevel.delete();
            }

            try {
                Files.write(fLevel.toPath(), jLevel.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e1) {
                Logger.error(e);
            }
        });
        add(saveButton);
    }

    private JButton createButton(String toolTip, String iconFileName,
            ActionListener listener) {
        JButton button = new JButton();

        // Looking for the icon
        String iconPath = "assets/editor/icons/" + iconFileName;
        Image image = KvadratikEditor.ASSETS.image(iconPath);

        button.setIcon(new ImageIcon(image));
//      button.setActionCommand("what's this");
        button.setToolTipText(toolTip);
        button.addActionListener(listener);
        return button;
    }

}
