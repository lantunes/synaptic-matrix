package com.algoritica.neurons.matrix;

import com.algoritica.neurons.mnist.MnistManager;
import com.google.common.io.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This demo emulates the way Trehub classified hand-written digits, as described in
 * chapter 11 of The Cognitive Brain. MNIST images are centered and size-normalized, so there is not so
 * much invariance between examples as one would find in practice. Thus, the retinoid/centroid system he
 * uses before sending the image to the synaptic matrix is not so critical here.
 *
 * Outline of procedure:
 * 1. get the next example
 * 2. test it against the network
 *   - if it is correct, go to the next example
 *   - if it is incorrect, train with the example on the next available class cell
 * ** instead of creating a new class cell each time, another way would be to simply initialize the matrix
 *    with as many class cells as there are examples, and use the next one available
 *
 * If we simply accumulated the weights in the synapses of the same class cell for each digit, then
 * we would end up with what Trehub calls some "normative prototype" (see pages 199-200 of The Cognitive Brain).
 * He claims the procedure described in this demo is superior.
 *
 * With b=1, c=20, and k=1500, this synaptic matrix achieves a classification accuracy of 88.95%.
 * Might this be improved by using a retinoid/centroid, and/or by taking into account the grayscale values?
 */
public class MnistDemoByAddingClassCells {

    public static void main(String[] args) throws Exception {

        SynapticMatrix synapticMatrix = new SynapticMatrix(784, 0, new SynapticConfig(1, 20, 1500));

        //map of labels to the indices of the class cells that correspond to them
        Map<Integer, List<Integer>> labelToClassCells = new HashMap<>();

        //train
        String imagesFile = Resources.getResource("mnist-handwritten/train-images.idx3-ubyte").getFile();
        String labelsFile = Resources.getResource("mnist-handwritten/train-labels.idx1-ubyte").getFile();
        MnistManager mnistManager = new MnistManager(imagesFile, labelsFile);
        for (int i = 0; i < 60000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input =  MatrixUtils.toLinearArray(image);
            int label = mnistManager.readLabel();
            System.out.println("presenting with training example #" + current + " (label: " + label + ")...");

            int[] relativeSpikeFrequencies = synapticMatrix.evaluate(input);
            int predictedClass = getPredictedClass(relativeSpikeFrequencies);

            if (labelToClassCells.get(label) == null || !labelToClassCells.get(label).contains(predictedClass)) {
                //there was a prediction error; train
                if (labelToClassCells.get(label) == null) {
                    labelToClassCells.put(label, new ArrayList<>());
                }
                int classCell = synapticMatrix.train(input);
                labelToClassCells.get(label).add(classCell);
                System.out.println("prediction incorrect; trained next available class cell (" + classCell + ")");
            } else {
                System.out.println("prediction correct");
            }
        }

        //evaluate
        int numCorrect = evaluateMNIST(synapticMatrix, labelToClassCells);

        System.out.println("------------------------");
        System.out.println("number of correct predictions: " + numCorrect + " (out of 10,000)");
    }

    private static int getPredictedClass(int[] relativeSpikeFrequencies) {
        if (allElementsTheSame(relativeSpikeFrequencies)) {
            return -1;
        }
        int highestRelativeSpikeFrequency = 0;
        int predictedClass = 0;
        for (int c = 0; c < relativeSpikeFrequencies.length; c++) {
            if (relativeSpikeFrequencies[c] > highestRelativeSpikeFrequency) {
                highestRelativeSpikeFrequency = relativeSpikeFrequencies[c];
                predictedClass = c;
            }
        }
        return predictedClass;
    }

    private static boolean allElementsTheSame(int[] array) {
        if (array.length == 0) {
            return true;
        } else {
            int first = array[0];
            for (int element : array) {
                if (element != first) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int evaluateMNIST(SynapticMatrix synapticMatrix, Map<Integer, List<Integer>> labelToClassCells) throws Exception {
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
                if (relativeSpikeFrequencies[c] > highestRelativeSpikeFrequency) {
                    highestRelativeSpikeFrequency = relativeSpikeFrequencies[c];
                    predictedClass = c;
                }
            }
            System.out.println("predicted class: " + predictedClass);
            if (labelToClassCells.get(label).contains(predictedClass)) {
                numCorrect++;
            }
        }
        return numCorrect;
    }
}