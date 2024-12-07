package com.github.inc0grepoz.kvad.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Canvas extends JPanel {

    protected double startTime = System.currentTimeMillis();
    protected double lastRefresh = 0;
    private double drawFps = 0;
    private double maxFps = 120;
    private double lastFpsCheck = 0;
    private int checks = 0;

    @Override
    public void paintComponent(Graphics gfx) {
        int cWidth = getWidth(), cHeight = getHeight();
        BufferedImage image = new BufferedImage(cWidth, cHeight, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = image.createGraphics();

        update(g2d);
        sleepAndRefresh();

        g2d.dispose();
        gfx.drawImage(image, 0, 0, cWidth, cHeight, this);
    }

    public void setFrapsPerSecond(int cap) {
        maxFps = cap;
    }

    public int getFrapsPerSecond() {
        return (int) drawFps;
    }

    protected abstract void update(Graphics2D g2d);

    private void sleepAndRefresh() {
        long timeSLU = (long) (System.currentTimeMillis() - lastRefresh);

        if (++checks >= 15) {
            drawFps = checks / ((System.currentTimeMillis() - lastFpsCheck) / 1000d);
            lastFpsCheck = System.currentTimeMillis();
            checks = 0;
        }

        if (timeSLU < 1000d / maxFps) {
            try {
                Thread.sleep((long) (1000d / maxFps - timeSLU));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lastRefresh = System.currentTimeMillis();
        repaint();
    }

}
