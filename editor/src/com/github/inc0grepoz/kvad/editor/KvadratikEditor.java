package com.github.inc0grepoz.kvad.editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

import com.github.inc0grepoz.kvad.editor.components.CanvasDropTarget;
import com.github.inc0grepoz.kvad.editor.components.ObjectList;
import com.github.inc0grepoz.kvad.editor.listeners.CanvasMouseListener;
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

    private final KvadratikCanvas canvas;
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
        canvas = new KvadratikCanvas(this, 640, 480);
        canvas.setBounds(0, 0, 640, 480);
        canvas.setBackground(Color.BLACK);
//      add(canvas);
        canvas.setFrapsPerSecond(20);
        canvas.getWorker().start();

        // Adding Drag & Drop feature support
        CanvasDropTarget dropTarget = new CanvasDropTarget(this);
        canvas.setDropTarget(dropTarget);

        CanvasMouseListener cml = new CanvasMouseListener(canvas);
        canvas.addMouseListener(cml);

        // Controls
        controls = new Controls(this);
        addKeysListener(canvas);

        // Physics (100 heartbeats per second)
        physics = new PhysicsWorker(this, 50L);
        physics.start();

        // Editor panels
        JPanel jpGeneral = new JPanel();
//      jpGeneral.setBorder(BorderFactory.createEtchedBorder());
        jpGeneral.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 10));

        JPanel jpCanvas = new JPanel();
        jpCanvas.add(canvas);
        jpGeneral.add(jpCanvas);

//      JLabel jlTest = new JLabel("Editor");
//      jpGeneral.add(jlTest);

        ObjectList jlObjects = new ObjectList();
        jpGeneral.add(jlObjects);

        add(jpGeneral);
        pack();
    }

    public void run() {
        String levelJson = ASSETS.textFile("assets/levels/whitespace.json");
        level = JSON.fromJsonLevel(this, levelJson, false);
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

    public KvadratikCanvas getCanvas() {
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

    public void addKeysListener(Canvas c) {
        c.addKeyListener(controls);
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
