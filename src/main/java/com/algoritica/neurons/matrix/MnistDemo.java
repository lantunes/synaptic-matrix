package com.algoritica.neurons.matrix;

import com.algoritica.neurons.mnist.MnistManager;
import com.google.common.io.Resources;

/**
 * With b=1, c=20, and k=1500, this synaptic matrix achieves a classification accuracy of 71.19%.
 * The images in the MNIST data set are not black and white--they contain grey levels
 * (i.e. instead of 0 and 255, it contains a range of values from 0-255). However, the synaptic matrix algorithm
 * currently ignores the absolute values of the input, and treats all input values as binary
 * (e.g. 0->0, 253->1, 15->1, etc.). This *may* make it  more difficult for the matrix to differentiate certain characters.
 *
 * One thing to try would be to simply use the value of the input instead of k/N when updating the weights, and get rid of c.
 *
 * Another thing to try would be to perhaps try to stack another synaptic matrix or two together,
 * and create a "deep" synaptic matrix.
 */
public class MnistDemo {

    public static void main(String[] args) throws Exception {

        SynapticMatrix synapticMatrix = new SynapticMatrix(784, 10, new SynapticConfig(1, 20, 1500));

        //train
        String imagesFile = Resources.getResource("mnist-handwritten/train-images.idx3-ubyte").getFile();
        String labelsFile = Resources.getResource("mnist-handwritten/train-labels.idx1-ubyte").getFile();
        MnistManager mnistManager = new MnistManager(imagesFile, labelsFile);
        for (int i = 0; i < 60000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input = toLinearArray(image);
            int label = mnistManager.readLabel();
            System.out.println("training with training example #" + current + " (label: " + label + ")");
            synapticMatrix.train(input, label);
        }

        System.out.println("------------------------");

        //evaluate
        imagesFile = Resources.getResource("mnist-handwritten/t10k-images.idx3-ubyte").getFile();
        labelsFile = Resources.getResource("mnist-handwritten/t10k-labels.idx1-ubyte").getFile();
        mnistManager = new MnistManager(imagesFile, labelsFile);
        int numCorrect = 0;
        for (int i = 0; i < 10000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input = toLinearArray(image);
            int label = mnistManager.readLabel();
            System.out.println("evaluating for test example #" + current + "(label: " + label + ")...");
            int[] relativeSpikeFrequencies = synapticMatrix.evaluate(input);

            int highestRelativeSpikeFrequency = 0;
            int predictedClass = 0;
            for (int c = 0; c < relativeSpikeFrequencies.length; c++) {
                System.out.println("  Class " + c + ": " + relativeSpikeFrequencies[c]);
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

        System.out.println("------------------------");
        System.out.println("number of correct predictions: " + numCorrect + " (out of 10,000)");
    }

    private static int[] toLinearArray(int[][] arr) {
        int[] linearArray = new int[arr.length * arr[0].length];
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                linearArray[pos++] = arr[i][k];
            }
        }
        return linearArray;
    }
}
