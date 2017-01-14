package org.algoritica.neurons.concurrent;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

public abstract class ConcurrentCognitiveComponent extends Thread {

    protected final int id;
    private final CountDownLatch countDownLatch;

    private boolean active;

    public ConcurrentCognitiveComponent(int id, CountDownLatch countDownLatch) {
        this.id = id;
        this.countDownLatch = countDownLatch;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        active = true;
        onStart();
        while (active) {
            try {
                process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onStart() {
    }

    protected abstract void process() throws SuspendExecution, InterruptedException;
}
