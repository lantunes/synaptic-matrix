package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

/*
 * This component emulates the activity and role of a dopaminergic neuron.
 * It is not a leaky integrator like a neuron. It simply takes input from
 * the critic module and the output of the network (in the form of inhibition),
 * and updates the weights of specific synapses.
 *
 * see Kurzawa et al., Neural Computation 29, 1–26 (2017)
 */
public class Modulator extends ConcurrentCognitiveComponent {

    public Modulator(int id, CountDownLatch countDownLatch) {
        super(id, countDownLatch);
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

    }
}
