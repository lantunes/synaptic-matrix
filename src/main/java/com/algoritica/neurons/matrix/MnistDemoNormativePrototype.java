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
 * -- this was tried and resulted in a 62.04% accuracy
 *
 * Another thing to try would be to perhaps try to stack another synaptic matrix or two together,
 * and create a "deep" synaptic matrix. We can try to create a convolutional synaptic matrix. We can create a number of
 * synaptic matrices of smaller size that correspond to smaller, overlapping regions of the image. These matrices feed
 * up to a master matrix.
 *
 * We can also try to understand what Trehub means by introducing an Imaging Matrix in addition to the Detection Matrix.
 *
 * Part of the problem may be that the digits are not scaled and/or centered the same way across the examples.
 * Indeed, Trehub uses a retinoid that attempts to translate the example to the normal foveal axis before learning.
 * However, this was on the web:
 *   '...for the MNIST digit classification problem we've been studying, the images are centered
 *    and size-normalized. So MNIST has less translation invariance than images found "in the wild", so
 *    to speak. Still, features like edges and corners are likely to be useful across much of the input space.'
 */
public class MnistDemoNormativePrototype {

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
            int[] input =  MatrixUtils.toLinearArray(image);
            int label = mnistManager.readLabel();
            System.out.println("training with training example #" + current + " (label: " + label + ")");
            synapticMatrix.train(input, label);
        }

        System.out.println("------------------------");

        //evaluate
        int numCorrect = evaluateMNIST(synapticMatrix, true);

        System.out.println("------------------------");
        System.out.println("number of correct predictions: " + numCorrect + " (out of 10,000)");
    }

    private static int evaluateMNIST(SynapticMatrix synapticMatrix, boolean verbose) throws Exception {
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
