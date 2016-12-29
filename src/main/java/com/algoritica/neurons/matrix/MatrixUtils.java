package com.algoritica.neurons.matrix;

import com.algoritica.neurons.mnist.MnistManager;
import com.google.common.io.Resources;

public class MatrixUtils {

    public static int[] toLinearArray(int[][] arr) {
        int[] linearArray = new int[arr.length * arr[0].length];
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                linearArray[pos++] = arr[i][k];
            }
        }
        return linearArray;
    }

    public static int evaluateMNIST(SynapticMatrix synapticMatrix, boolean verbose) throws Exception {
        String imagesFile = Resources.getResource("mnist-handwritten/t10k-images.idx3-ubyte").getFile();
        String labelsFile = Resources.getResource("mnist-handwritten/t10k-labels.idx1-ubyte").getFile();
        MnistManager mnistManager = new MnistManager(imagesFile, labelsFile);
        int numCorrect = 0;
        for (int i = 0; i < 10000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input = MatrixUtils.toLinearArray(image);
            int label = mnistManager.readLabel();
            System.out.println("evaluating for test example #" + current + "(label: " + label + ")...");
            int[] relativeSpikeFrequencies = synapticMatrix.evaluate(input);

            int highestRelativeSpikeFrequency = 0;
            int predictedClass = 0;
            for (int c = 0; c < relativeSpikeFrequencies.length; c++) {
                if (verbose) {
                    System.out.println("  Class " + c + ": " + relativeSpikeFrequencies[c]);
                }
                if (relativeSpikeFrequencies[c] > highestRelativeSpikeFrequency) {
                    highestRelativeSpikeFrequency = relativeSpikeFrequencies[c];
                    predictedClass = c;
                }
            }
            System.out.println("predicted class: " + predictedClass);
            if (predictedClass == label) {
                numCorrect++;
            }
        }
        return numCorrect;
    }
}
