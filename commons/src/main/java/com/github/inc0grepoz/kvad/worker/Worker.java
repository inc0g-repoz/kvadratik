package com.github.inc0grepoz.kvad.worker;

import com.github.inc0grepoz.kvad.utils.Logger;

import lombok.Getter;

public abstract class Worker {

    private final Thread thread;
    private @Getter long delay;
    private @Getter boolean executing, running;

    protected Worker(long delay) {
        this.delay = delay;

        thread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(this.delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                executing = true;
                work();
                executing = false;
            }
        });
        thread.setName(getClass().getSimpleName());
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void start() {
        if (!running) {
            running = true;

            thread.start();
            Logger.info("Started " + thread.getName() + " thread");
        }
    }

    public void kill() {
        running = false;
        Logger.info("Killed " + thread.getName() + " thread");
    }

    protected abstract void work();

}
