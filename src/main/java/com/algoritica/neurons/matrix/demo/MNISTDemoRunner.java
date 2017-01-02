package com.algoritica.neurons.matrix.demo;

import com.algoritica.neurons.matrix.MatrixUtils;
import com.algoritica.neurons.matrix.SynapticMatrix;
import com.algoritica.neurons.mnist.MNISTManager;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MNISTDemoRunner {

    private static final Logger logger = LoggerFactory.getLogger(MNISTDemoRunner.class);

    public static void run(SynapticMatrix synapticMatrix) throws Exception {
        //train
        String imagesFile = Resources.getResource("mnist-handwritten/train-images.idx3-ubyte").getFile();
        String labelsFile = Resources.getResource("mnist-handwritten/train-labels.idx1-ubyte").getFile();
        MNISTManager mnistManager = new MNISTManager(imagesFile, labelsFile);
        for (int i = 0; i < 60000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input =  MatrixUtils.toLinearArray(image);
            int label = mnistManager.readLabel();
            logger.info("training with training example #" + current + " (label: " + label + ")");
            synapticMatrix.train(input, label);
        }

        logger.info("------------------------");

        //evaluate
        int numCorrect = evaluateMNIST(synapticMatrix);

        logger.info("------------------------");
        logger.info("number of correct predictions: " + numCorrect + " (out of 10,000)");
    }

    private static int evaluateMNIST(SynapticMatrix synapticMatrix) throws Exception {
        String imagesFile = Resources.getResource("mnist-handwritten/t10k-images.idx3-ubyte").getFile();
        String labelsFile = Resources.getResource("mnist-handwritten/t10k-labels.idx1-ubyte").getFile();
        MNISTManager mnistManager = new MNISTManager(imagesFile, labelsFile);
        int numCorrect = 0;
        for (int i = 0; i < 10000; i++) {
            int current = i + 1;
            mnistManager.setCurrent(current);
            int[][] image = mnistManager.readImage();
            int[] input = MatrixUtils.toLinearArray(image);
            int label = mnistManager.readLabel();
            logger.info("evaluating for test example #" + current + "(label: " + label + ")...");
            int[] relativeSpikeFrequencies = synapticMatrix.evaluate(input);

            int highestRelativeSpikeFrequency = 0;
            int predictedClass = 0;
            for (int c = 0; c < relativeSpikeFrequencies.length; c++) {
                logger.info("  Class " + c + ": " + relativeSpikeFrequencies[c]);
                if (relativeSpikeFrequencies[c] > highestRelativeSpikeFrequency) {
                    highestRelativeSpikeFrequency = relativeSpikeFrequencies[c];
                    predictedClass = c;
                }
            }
            logger.info("predicted class: " + predictedClass);
            if (predictedClass == label) {
                numCorrect++;
            }
        }
        return numCorrect;
    }
}
