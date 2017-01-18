package org.algoritica.neurons.matrix;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SupervisedSynapticMatrix implements SynapticMatrix<SupervisedSynapticMatrix> {

    private static final Logger logger = LoggerFactory.getLogger(SupervisedSynapticMatrix.class);

    private final BasicSynapticMatrix matrix;

    private final Map<Integer, Set<Integer>> labelToClassCells = new HashMap<>();

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
        PriorityQueue<ActivityAndClass> heap = matrix.heap(example);
        int predictedClass = (heap.size() == 0) ? -1 : heap.peek().getClassCellNumber();

        if (labelToClassCells.get(label) == null || !labelToClassCells.get(label).contains(predictedClass)) {
            //there was a prediction error; train
            if (labelToClassCells.get(label) == null) {
                labelToClassCells.put(label, new HashSet<>());
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

    public int getNumClassCells() {
        return classCellsToLabels.keySet().size();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject root = new JSONObject();
        JSONObject basicSynapticMatrixJSON = matrix.toJSON();
        root.put("basic", basicSynapticMatrixJSON);
        return root;
    }

    @Override
    public SupervisedSynapticMatrix fromJSON(JSONObject root) {
        return null;
    }
}
