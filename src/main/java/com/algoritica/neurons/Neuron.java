package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;
import co.paralleluniverse.strands.concurrent.CountDownLatch;
import javolution.util.FastTable;

public class Neuron extends ConcurrentCognitiveComponent {

    private final IntChannel incoming;
    private final FastTable<IntChannel> outgoing = new FastTable<>();

    private final int threshold;

    /*
     * number of potential units leaked per millisecond;
     * e.g. a leak rate of 0.1 means 1 potential unit will be lost every 10 ms
     *      a leak rate of 0.001 means 1 potential unit will be lost every 1 s
     */
    private final double leakRate;

    private int potential = 0;

    private long lastInputTimeNano;

    public Neuron(int id, CountDownLatch countDownLatch, int threshold, double leakRate, IntChannel ... out) {
        super(id, countDownLatch);
        this.incoming = Channels.newIntChannel(-1, Channels.OverflowPolicy.BLOCK, false, true);
        this.threshold = threshold;
        this.leakRate = leakRate;
        this.outgoing.addAll(out);
    }

    public IntChannel incoming() {
        return incoming;
    }

    public void addOutgoing(IntChannel out) {
        outgoing.add(out);
    }

    @Override
    protected void onStart() {
        lastInputTimeNano = System.nanoTime();
    }

    @Override
    protected void process() throws SuspendExecution, InterruptedException {

        int input = incoming.receive();

        //subtract leak amount since last input
        long now = System.nanoTime();
        int leakAmount = amountLeaked(now);
        lastInputTimeNano = now;
        potential = (leakAmount > potential) ? 0 : potential - leakAmount;

        //integrate input
        potential += input;
        System.out.println("[neuron " + id + "][" + System.nanoTime() + " ns][INPUT]: " + input +
                " {potential: " + potential + ", leaked: " + leakAmount + "}");

        if (potential > threshold) {
            //spike
            System.out.println("[neuron " + id + "][" + System.nanoTime() + " ns][SPIKE]");
            for (IntChannel out : outgoing) {
                out.send(potential);
            }

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
