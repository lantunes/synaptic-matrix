package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

public class Neuron extends ConcurrentCognitiveComponent {

    private final Channel<ActionPotential> incoming;
    private final Channel<ActionPotential> outgoing;

    private final int threshold;

    /*
     * number of potential units leaked per millisecond;
     * e.g. a leak rate of 0.1 means 1 potential unit will be lost every 10 ms
     *      a leak rate of 0.001 means 1 potential unit will be lost every 1 s
     */
    private final double leakRate;

    private int potential = 0;

    private long lastInputTimeNano;

    public Neuron(int id, CountDownLatch countDownLatch, int threshold, double leakRate) {
        super(id, countDownLatch);
        this.incoming = Channels.newChannel(-1, Channels.OverflowPolicy.BLOCK, false, true);
        /*
         * a neuron can be connected to up to 100 downstream synapses before it might block while sending
         * an action potential downstream; this number can be bumped up if required
         */
        this.outgoing = Channels.newChannel(100, Channels.OverflowPolicy.BLOCK, true, false);
        this.threshold = threshold;
        this.leakRate = leakRate;
    }

    public Channel<ActionPotential> incoming() {
        return incoming;
    }

    public Channel<ActionPotential> outgoing() {
        return outgoing;
    }

    @Override
    protected void onStart() {
        lastInputTimeNano = System.nanoTime();
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

        ActionPotential input = incoming.receive();

        /* subtract leak amount since last input */
        long now = System.nanoTime();
        int leakAmount = amountLeaked(now);
        lastInputTimeNano = now;
        potential = (leakAmount > potential) ? 0 : potential - leakAmount;

        /* integrate input */
        potential += input.get();
        System.out.println("[neuron " + id + "][" + System.nanoTime() + " ns][INPUT]: " + input +
                " {potential: " + potential + ", leaked: " + leakAmount + "}");

        if (potential > threshold) {
            /* spike */
            System.out.println("[neuron " + id + "][" + System.nanoTime() + " ns][SPIKE]");
            /*
             * Since 1 is always the activation value sent, the activation function is a
             * binary step function, with a range of {0,1}.
             *
             * Could we replace this value with some function of the potential, such as a sigmoidal function?
             * What would be the biological relevance of the resulting value in this context?
             * In a traditional neural net, this value could be interpreted as the
             * "firing rate", or "activity" of the neuron. In common neural nets, activation functions are
             * usually sigmoidal because neurons max out their firing rate at some fixed level. This is the biological
             * relevance of sigmoidal activation functions.
             *
             * A difference is that, in a common neural net, the activation function is always computed,
             * regardless of the value of the summation (of inputs*weights); whereas what's being proposed here,
             * is that the activation function is invoked only if a threshold is surpassed.
             *
             * With this concurrent neuronal system, it seems that, to achieve the result of sigmoidal activation in
             * common neural nets, the neuron's spiking behaviour over a time range would have to be considered.
             *
             * From: http://stackoverflow.com/questions/15640195/how-to-determine-the-threshold-for-neuron-firings-in-neural-networks
             * "be careful: a perceptron with a simple threshold activation function can only be correct if your data
             * are linearly separable. If you have more complex data you will need a Multilayer Perceptron and a
             * nonlinear activation function like the Logistic Sigmoid Function"
             * Multilayer neural networks with a simple threshold activation function have less representational power than the same
             * network with a sigmoidal activation function, requiring more neurons and connections. - source unknown
             *
             * In a common neural net, what does it mean for a "firing rate" to be multiplied by a "synaptic weight"?
             * In this sense, common neural nets are unintuitive and not very biologically relevant.
             *
             * With this concurrent neuronal system, we have to achieve the same kind of representational power as common neural
             * nets. But how? We can consider two things: a range of time, and the number of spikes of the neuron in
             * that time range, instead of just whether a neuron fires or not.
             * For example: We deliver the sensory inputs every 10 ms over a period of 50 ms. Then, we measure how many
             * times, (and when?), the neuron in question fires in that period. By adjusting the weights, the neuron can learn to fire
             * more times or fewer times during that same period. We can say that the network has learned to classify an input
             * when the pattern is the same for all inputs of a given class. For example, below is a network that has learned
             * the XOR function (though we can say it has also learned the AND and NOR functions):
             *
             *         |-------------50 ms---------|
             * input:  |---0,1---0,1---0,1---0,1---|
             * output: |------------*-----------*--|
             *
             *         |------------50 ms----------|
             * input:  |---1,0---1,0---1,0---1,0---|
             * output: |------------*-----------*--|
             *
             *         |------------50 ms----------|
             * input:  |---0,0---0,0---0,0---0,0---|
             * output: |---------------------------|
             *
             *         |------------50 ms----------|
             * input:  |---1,1---1,1---1,1---1,1---|
             * output: |------*-----*-----*-----*--|
             *
             * where * represents a spike, for the network:
             * [network
             *   [synapse
             *     id 1
             *     weight 3
             *   ]
             *   [synapse
             *     id 2
             *     weight 3
             *   ]
             *   [neuron
             *     id 0
             *     threshold 5
             *     [dendrite
             *       synapse 1
             *       synapse 2
             *     ]
             *   ]
             * ]
             *
             * TODO:
             * When setting up a network, how do we know what output spike rates are possible given the configuration?
             * For example, say we want to learn to classify hand written letters.. what spike rates should we assign
             * the various letters during training?
             *
             * The initial fear is that a simple feed-forward multilayer perceptron with a simple step activation function
             * cannot learn much. However, that is in the context of traditional neural nets, that use backprop of error to learn.
             * In this concurrent approach, learning is local. We may not be able to learn XOR with a single neuron,
             * but that doesn't mean it is not worthwhile pursuing.
             */
            outgoing.send(new ActionPotential(1));

            //send a message back to incoming synapses for STDP
            //TODO this isn't going to work, as it will trigger incoming.receive() in this process
//            incoming.send(potential);

            potential = 0;
        }
    }

    private int amountLeaked(long now) {
        long timeElapsedNano = now - lastInputTimeNano;
        long timeElapsedMillis = timeElapsedNano / 1000000;
        return (int)(timeElapsedMillis * leakRate);
    }
}
