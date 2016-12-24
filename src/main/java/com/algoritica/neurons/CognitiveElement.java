package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

public abstract class CognitiveElement extends Thread {

    protected final int id;
    private final CountDownLatch countDownLatch;

    private boolean active;

    public CognitiveElement(int id, CountDownLatch countDownLatch) {
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
        while (active) {
            try {
                process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void process() throws SuspendExecution, InterruptedException;
}
