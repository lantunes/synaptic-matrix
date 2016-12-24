package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;
import co.paralleluniverse.strands.concurrent.CountDownLatch;
import javolution.util.FastTable;

import java.util.List;

public class Neuron extends CognitiveElement {

    private final IntChannel incoming;
    private final FastTable<IntChannel> outgoing = new FastTable<>();

    private final int threshold = 5;

    private int current = 0;

    public Neuron(int id, CountDownLatch countDownLatch, List<IntChannel> out) {
        super(id, countDownLatch);
        this.incoming = Channels.newIntChannel(-1, Channels.OverflowPolicy.BLOCK, false, true);
        if (out != null) {
            this.outgoing.addAll(out);
        }
    }

    public IntChannel incoming() {
        return incoming;
    }

    public void addOutgoing(IntChannel out) {
        outgoing.add(out);
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

        int input = incoming.receive();
        current += input;
        System.out.println("[neuron " + id + "] - input received: " + input + " {current: " + current + "}");

        if (current > threshold) {
            //spike
            System.out.println("[neuron " + id + "] - SPIKE");
            for (IntChannel out : outgoing) {
                out.send(current);
            }

            //send a message back to incoming synapses for STDP
            //TODO this isn't going to work, as it will trigger incoming.receive() in this process
//            incoming.send(current);

            current = 0;
        }

        //TODO leak
    }
}
