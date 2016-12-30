package com.algoritica.neurons.matrix;

public class XORDemo {

    public static void main(String[] args) throws Exception {

        SynapticMatrix synapticMatrix = new SynapticMatrix(2, 0, new SynapticConfig(1, 2, 18));

        int xorClassCell1 = synapticMatrix.train(new int[]{1,0});
        int xorClassCell2 = synapticMatrix.train(new int[]{0,1});
        int xorClassCell3 = synapticMatrix.train(new int[]{1,1});

        System.out.println("XOR Class Cell for [1,0]: " + xorClassCell1);
        System.out.println("XOR Class Cell for [0,1]: " + xorClassCell2);
        System.out.println("XOR Class Cell for [1,1]: " + xorClassCell3);

        int[] relativeSpikeFrequencies = synapticMatrix.evaluate(
                new int[]{1,0}
        );

        for (int i = 0; i < relativeSpikeFrequencies.length; i++) {
            System.out.println("Class " + i + ": " + relativeSpikeFrequencies[i]);
        }
    }
}
