package com.github.inc0grepoz.kvad.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.github.inc0grepoz.kvad.editor.awt.CanvasDropTarget;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseMotionListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasRenderer;
import com.github.inc0grepoz.kvad.editor.awt.ObjectList;
import com.github.inc0grepoz.kvad.editor.awt.SelectionModesComboBox;
import com.github.inc0grepoz.kvad.entities.BeingFactory;
import com.github.inc0grepoz.kvad.entities.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

@SuppressWarnings("serial")
public class KvadratikEditor extends Frame {

    public static final AssetsManager ASSETS = new AssetsManager();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();

    public final ObjectList jlObjects = new ObjectList();
    public final Selection selection = new Selection(this);

    private final CanvasRenderer canvas;
    private final PhysicsWorker physics;
    private final Controls controls;

    private Level level;

    {
        addWindowListener(() -> {
            dispose();
            System.exit(0);
        });

        setTitle("kvadratik editor");
        applyIcon("assets/icon.png");

        // Rendering
        canvas = new CanvasRenderer(this, 640, 480);
        canvas.setBounds(0, 0, 640, 480);
        canvas.setBackground(Color.BLACK);
//      add(canvas);
        canvas.setFrapsPerSecond(20);
        canvas.getWorker().start();

        // Canvas Drag & Drop
        CanvasDropTarget dropTarget = new CanvasDropTarget(this);
        canvas.setDropTarget(dropTarget);

        MouseListener cml = new CanvasMouseListener(canvas);
        MouseMotionListener cmml = new CanvasMouseMotionListener(canvas);
        canvas.addMouseListener(cml);
        canvas.addMouseMotionListener(cmml);

        // Controls
        controls = new Controls(this);
        canvas.addKeyListener(controls);

        // Physics (100 heartbeats per second)
        physics = new PhysicsWorker(this, 50L);
        physics.start();

        // Usable in the object initializers
        KvadratikEditor editor = this;

        // Tools panel
        JPanel jpTools = new JPanel() {

            {
                // Components are place vertically
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                // Selector modes
                SelectionModesComboBox selModes = new SelectionModesComboBox(editor);
                add(selModes);

                // Level objects list
                add(jlObjects);
            }

        };

        // Editor panels
        JPanel jpGeneral = new JPanel() {

            {
//              setBorder(BorderFactory.createEtchedBorder());

//              JPanel jpCanvas = new JPanel();
                add(canvas, BorderLayout.WEST);

//              add(jpCanvas);
                add(jpTools);
            }

        };

        add(jpGeneral, BorderLayout.WEST);
    }

    public void run() {
        String levelJson = ASSETS.textFile("assets/levels/whitespace.json");
        level = JSON.fromJsonLevel(this, levelJson, false);
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

    public CanvasRenderer getCanvas() {
        return canvas;
    }

    public Controls getControls() {
        return controls;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void addWindowListener(Runnable handler) {
        WindowAdapter adapter = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                handler.run();
            }

        };
        addWindowListener(adapter);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            adapter.windowClosed(null);
        }));
    }

}
