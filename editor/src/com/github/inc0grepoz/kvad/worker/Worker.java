package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.utils.Logger;

public abstract class Worker {

    protected Thread thread;
    protected long delay;
    protected boolean alive;

    protected Worker(long delay) {
        this.delay = delay;

        thread = new Thread(() -> {
            while (alive) {
                try {
                    Thread.sleep(this.delay);
                    work();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName(getClass().getSimpleName());
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void start() {
        if (!alive) {
            alive = true;

            thread.start();
            Logger.info("Started " + thread.getName() + " thread");
        }
    }

    public void kill() {
        alive = false;
        Logger.info("Killed " + thread.getName() + " thread");
    }

    public boolean isRunning() {
        return alive;
    }

    protected abstract void work();

}
