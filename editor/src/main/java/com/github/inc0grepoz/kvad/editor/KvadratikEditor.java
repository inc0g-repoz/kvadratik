package com.github.inc0grepoz.kvad.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import com.github.inc0grepoz.commons.util.json.mapper.JsonMapper;
import com.github.inc0grepoz.kvad.common.Kvadratik;
import com.github.inc0grepoz.kvad.common.entities.factory.BeingFactory;
import com.github.inc0grepoz.kvad.common.entities.factory.LevelObjectFactory;
import com.github.inc0grepoz.kvad.common.entities.level.Level;
import com.github.inc0grepoz.kvad.common.utils.AssetsProvider;
import com.github.inc0grepoz.kvad.common.utils.Platform;
import com.github.inc0grepoz.kvad.editor.awt.CanvasDropTarget;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasMouseMotionListener;
import com.github.inc0grepoz.kvad.editor.awt.CanvasRenderer;
import com.github.inc0grepoz.kvad.editor.awt.EditorToolBar;
import com.github.inc0grepoz.kvad.editor.awt.EditorToolsPanel;
import com.github.inc0grepoz.kvad.utils.JSON;
import com.github.inc0grepoz.kvad.worker.PhysicsWorker;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
public class KvadratikEditor extends Frame implements Kvadratik {

    public static final JsonMapper JSON_MAPPER = new JsonMapper();
    public static final AssetsProvider ASSETS = new AssetsProvider();
    public static final BeingFactory BEING_FACTORY = new BeingFactory();
    public static final LevelObjectFactory OBJECT_FACTORY = new LevelObjectFactory();
    public static final KvadratikEditor INSTANCE = Platform.init(new KvadratikEditor());

    private final @Getter EditorToolBar toolBar = new EditorToolBar(this);
    private final @Getter EditorToolsPanel panel = new EditorToolsPanel(this);
    private final @Getter Selection selection = new Selection(this);
    private final @Getter Controls controls;
    private final @Getter CanvasRenderer canvas;
    private final PhysicsWorker physics;

    private @Getter @Setter Level level;

    {
        if (INSTANCE != null) {
            throw new IllegalStateException("Editor instance already has been initialized");
        }

        addShutdownListener(() -> {
            dispose();
            System.exit(0);
        });

        // Rendering
        canvas = new CanvasRenderer(this, 640, 480);
        canvas.setBounds(0, 0, 640, 480);
        canvas.setBackground(Color.BLACK);

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

    @Override
    public AssetsProvider getAssetsProvider() {
        return ASSETS;
    }

    @Override
    public JsonMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    @Override
    public BeingFactory getBeingFactory() {
        return BEING_FACTORY;
    }

    @Override
    public LevelObjectFactory getLevelObjectFactory() {
        return OBJECT_FACTORY;
    }

    public void run() {
        String levelJson = ASSETS.textFile("assets/levels/default.json");
        level = JSON.fromJsonLevel(levelJson, false);
        level.setPath("assets/levels/default.json");
    }

    public void loadLevel(File file) {
        String levelJson = ASSETS.textFile(file);
        level = JSON.fromJsonLevel(levelJson, false);
        level.setPath(file.getPath());
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
