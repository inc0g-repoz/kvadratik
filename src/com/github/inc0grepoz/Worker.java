package com.github.inc0grepoz;

public abstract class Worker {

    protected Thread thread;
    protected long delay;
    protected boolean alive;

    public Worker(long delay) {
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
            System.out.println("Started " + thread.getName() + " thread");
        }
    }

    public void kill() {
        alive = false;
        System.out.println("Killed " + thread.getName() + " thread");
    }

    public boolean isRunning() {
        return alive;
    }

    public abstract void work();

}
