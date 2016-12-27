package com.algoritica.neurons.concurrent;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

public class Synapse extends ConcurrentCognitiveComponent {

    private final Channel<ActionPotential> incoming;
    private final Channel<ActionPotential> outgoing;

    private int weight;

    public Synapse(int id, CountDownLatch countDownLatch, int initialWeight,
                   Channel<ActionPotential> incoming, Channel<ActionPotential> outgoing) {
        super(id, countDownLatch);
        this.incoming = incoming;
        this.weight = initialWeight;
        this.outgoing = outgoing;
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

        ActionPotential input = incoming.receive();
        System.out.println("[synapse " + id + "][" + System.nanoTime() + " ns][INPUT]: " + input + " {weight: " + weight + "}");
        outgoing.send(new ActionPotential(input.get() * weight));

        //wait to see if downstream neuron fired for STDP
        //TODO this isn't going to work, as outgoing.send() in this process is going to trigger outgoing.receive()
//        Integer epsp = outgoing.receive(500, TimeUnit.MILLISECONDS);
//        if (epsp != null) {
//            System.out.println("[synapse " + id + "] - neuron fired - EPSP: " + epsp);
//            //update weight
//        }
    }
}
