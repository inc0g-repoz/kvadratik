package com.github.inc0grepoz.kvad.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.github.inc0grepoz.kvad.editor.awt.CanvasDropTarget;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseMotionListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasRenderer;
import com.github.inc0grepoz.kvad.editor.awt.EditorToolBar;
import com.github.inc0grepoz.kvad.editor.awt.EditorToolsPanel;
import com.github.inc0grepoz.kvad.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.utils.AssetsManager;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class KvadratikEditor extends Frame {

    public static final AssetsManager ASSETS = new AssetsManager();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();

    private final @Getter EditorToolBar toolBar = new EditorToolBar(this);
    private final @Getter EditorToolsPanel panel = new EditorToolsPanel(this);
    private final @Getter Selection selection = new Selection(this);
    private final @Getter Controls controls;
    private final @Getter CanvasRenderer canvas;
    private final PhysicsWorker physics;

    private @Getter @Setter Level level;

    {
        addShutdownListener(() -> {
            dispose();
            System.exit(0);
        });

        setTitle("kvadratik editor");
        applyIcon("assets/icon.png");

        // Rendering
        canvas = new CanvasRenderer(this, 640, 480);
        canvas.setBounds(0, 0, 640, 480);
        canvas.setBackground(Color.BLACK);
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

        // Physics (20 heartbeats per second)
        physics = new PhysicsWorker(this, 50L);
        physics.start();

        // Editor UI elements
        add(toolBar, BorderLayout.PAGE_START);
        add(canvas, BorderLayout.WEST);
        add(panel, BorderLayout.EAST);

//      pack();
    }

    public void run() {
        level = ASSETS.level(this, "assets/levels/level.json");
    }

    public void applyIcon(String fileName) {
        setIconImage(ASSETS.image(fileName));
    }

    public void addShutdownListener(Runnable handler) {
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
