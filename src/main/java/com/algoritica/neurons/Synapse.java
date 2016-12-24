package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;
import co.paralleluniverse.strands.concurrent.CountDownLatch;

import java.util.concurrent.TimeUnit;

public class Synapse extends CognitiveElement {

    private final IntChannel incoming;
    private final IntChannel outgoing;

    private int weight = 1;

    public Synapse(int id, CountDownLatch countDownLatch, IntChannel outgoing) {
        super(id, countDownLatch);
        this.incoming = Channels.newIntChannel(-1, Channels.OverflowPolicy.BLOCK, true, true);
        this.outgoing = outgoing;
    }

    public IntChannel incoming() {
        return incoming;
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

        int input = incoming.receive();
        System.out.println("[synapse " + id + "] - input received: " + input + " {weight: " + weight + "}");
        outgoing.send(input * weight);

        //wait to see if downstream neuron fired for STDP
        //TODO this isn't going to work, as outgoing.send() in this process is going to trigger outgoing.receive()
//        Integer epsp = outgoing.receive(500, TimeUnit.MILLISECONDS);
//        if (epsp != null) {
//            System.out.println("[synapse " + id + "] - neuron fired - EPSP: " + epsp);
//            //update weight
//        }
    }
}
