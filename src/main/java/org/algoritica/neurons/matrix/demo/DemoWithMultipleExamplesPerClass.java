package org.algoritica.neurons.matrix.demo;

import org.algoritica.neurons.matrix.BasicSynapticMatrix;
import org.algoritica.neurons.matrix.SynapticConfig;
import org.algoritica.neurons.matrix.SynapticMatrix;

public class DemoWithMultipleExamplesPerClass {

    public static void main(String[] args) throws Exception {

        SynapticMatrix synapticMatrix = new BasicSynapticMatrix(49, 2, new SynapticConfig(1, 10, 500));

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
