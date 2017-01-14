package org.algoritica.neurons.matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupervisedSynapticMatrix implements SynapticMatrix {

    private static final Logger logger = LoggerFactory.getLogger(SupervisedSynapticMatrix.class);

    private final BasicSynapticMatrix matrix;

    private final Map<Integer, List<Integer>> labelToClassCells = new HashMap<>();

    private final Map<Integer, Integer> classCellsToLabels = new HashMap<>();

    public SupervisedSynapticMatrix(int numInputCells, SynapticConfig config) {
        matrix = new BasicSynapticMatrix(numInputCells, 0, config);
    }

    @Override
    public int train(int[] inputExample) {
        throw new UnsupportedOperationException("a label must always be provided when training a supervised synaptic matrix");
    }

    /**
     * The inputExample must be an array containing only 0s and 1s.
     */
    public void train(int[] example, int label) {
        int[] relativeSpikeFrequencies = matrix.evaluate(example);
        int predictedClass = getPredictedClass(relativeSpikeFrequencies);

        if (labelToClassCells.get(label) == null || !labelToClassCells.get(label).contains(predictedClass)) {
            //there was a prediction error; train
            if (labelToClassCells.get(label) == null) {
                labelToClassCells.put(label, new ArrayList<>());
            }
            int classCell = matrix.train(example);
            labelToClassCells.get(label).add(classCell);
            classCellsToLabels.put(classCell, label);
            logger.info("prediction incorrect; trained next available class cell (" + classCell + ")");
        } else {
            logger.info("prediction correct");
        }
    }

    /**
     * The input must be an array containing only 0s and 1s.
     */
    public int[] evaluate(int[] input) {
        int[] allRelativeSpikeFrequencies = matrix.evaluate(input);
        int[] relativeSpikeFrequencies = new int[labelToClassCells.keySet().size()];
        for (int c = 0; c < allRelativeSpikeFrequencies.length; c++) {
            int label = classCellsToLabels.get(c);
            if (allRelativeSpikeFrequencies[c] > relativeSpikeFrequencies[label]) {
                relativeSpikeFrequencies[label] = allRelativeSpikeFrequencies[c];
            }
        }
        return relativeSpikeFrequencies;
    }

    private int getPredictedClass(int[] relativeSpikeFrequencies) {
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

    private boolean allElementsTheSame(int[] array) {
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
}
