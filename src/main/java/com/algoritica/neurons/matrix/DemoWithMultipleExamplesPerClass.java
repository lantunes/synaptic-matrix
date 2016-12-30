package com.algoritica.neurons.matrix;

public class DemoWithMultipleExamplesPerClass {

    public static void main(String[] args) throws Exception {

        SynapticMatrix synapticMatrix = new SynapticMatrix(49, 2, new SynapticConfig(1, 10, 500));

        synapticMatrix.train(
                new int[]{0,1,1,1,1,1,1,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0,
                          0,1,1,1,1,0,0,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0}, 0);
        synapticMatrix.train(
                new int[]{0,0,0,1,0,0,0,
                          0,0,1,0,1,0,0,
                          0,1,0,0,0,1,0,
                          0,1,1,1,1,1,0,
                          0,1,0,0,0,1,0,
                          0,1,0,0,0,1,0,
                          0,1,0,0,0,1,0}, 1);
        synapticMatrix.train(
                new int[]{0,0,0,0,0,0,0,
                          0,1,1,1,1,0,0,
                          0,1,0,0,0,0,0,
                          0,1,1,1,0,0,0,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0,
                          0,0,0,0,0,0,0}, 0);
        synapticMatrix.train(
                new int[]{0,0,0,0,0,0,0,
                          0,0,0,1,0,0,0,
                          0,0,1,0,1,0,0,
                          0,1,0,0,0,1,0,
                          0,1,1,1,1,1,0,
                          0,1,0,0,0,1,0,
                          0,0,0,0,0,0,0}, 1);

        int[] relativeSpikeFrequencies = synapticMatrix.evaluate(
                new int[]{0,0,0,0,0,0,0,
                          0,1,1,1,1,1,0,
                          0,1,0,0,0,0,0,
                          0,1,1,1,1,0,0,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0,
                          0,1,0,0,0,0,0}
        );

        for (int i = 0; i < relativeSpikeFrequencies.length; i++) {
            System.out.println("Class " + i + ": " + relativeSpikeFrequencies[i]);
        }
    }
}
