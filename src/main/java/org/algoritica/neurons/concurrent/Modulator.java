package org.algoritica.neurons.concurrent;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

/*
 * This component emulates the activity and role of a dopaminergic neuron.
 * It is not a leaky integrator like a neuron. It simply takes input from
 * the critic module and the output of the network (in the form of inhibition),
 * and updates the weights of specific synapses.
 *
 * see Kurzawa et al., Neural Computation 29, 1–26 (2017)
 *
 * As an alternative mechanism, consider that the critic module can
 * inform the modulator of a reward or punishment, and the modulator
 * can change synapses by making them Hebbian or anti-Hebbian, depending on
 * whether a reward or punishment was administered.
 *
 * see Florian et al. Neural Computation 19 (6), pp. 1468-1502, 2007.
 */
public class Modulator extends ConcurrentCognitiveComponent {

    public Modulator(int id, CountDownLatch countDownLatch) {
        super(id, countDownLatch);
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

    }
}
