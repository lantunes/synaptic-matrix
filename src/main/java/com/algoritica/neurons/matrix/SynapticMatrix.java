package com.algoritica.neurons.matrix;

import java.util.ArrayList;
import java.util.List;

/*
 * A synaptix matrix based on the work of Arnold Trehub.
 */
public class SynapticMatrix {

    private final int numInputCells;
    private final int numClassCells;
    private final SynapticConfig config;
    private final InputCell[] inputCells;
    private final ClassCell[] classCells;

    public SynapticMatrix(int numInputCells, int numClassCells, SynapticConfig config) {
        if (numInputCells < 1) {
            throw new IllegalArgumentException("there must be at least 1 input cell");
        }
        if (numClassCells < 1) {
            throw new IllegalArgumentException("there must be at least 1 class cell");
        }

        this.numInputCells = numInputCells;
        this.numClassCells = numClassCells;
        this.inputCells = new InputCell[numInputCells];
        this.classCells = new ClassCell[numClassCells];
        this.config = config;

        construct();
    }

    private void construct() {
        for (int i = 0; i < numInputCells; i++) {
            inputCells[i] = new InputCell("#" + (i+1));
            for (int c = 0; c < numClassCells; c++) {
                if (classCells[c] == null) {
                    classCells[c] = new ClassCell("#" + (c+1));
                }
                Synapse s = new Synapse(config.b(), inputCells[i], classCells[c]);
                inputCells[i].addAxonalSynapse(s);
                classCells[c].addDendriticSynapse(s);
            }
        }
    }

    /*
     * each item in the array is an example input corresponding to each class
     * e.g. given a = [[0,1],[1,0]],
     *   a[0] is the example input for class cell 0,
     *   a[1] is the example input for class cell 1
     */
    public void train(int[][] examples) {
        if (examples.length < 1) {
            throw new IllegalArgumentException("there are no examples");
        }
        if (examples.length != numClassCells) {
            throw new IllegalArgumentException("the number of examples must be the same as the number of class cells");
        }

        for (int e = 0; e < examples.length; e++) {
            train(examples[e], e);
        }
    }

    public void train(int[] inputExample, int classCell) {
        if (inputExample.length != numInputCells) {
            throw new IllegalArgumentException("the number of inputs in example " + classCell +
                    " does not equal the number of input cells");
        }

        List<Synapse> eligibleSynapses = new ArrayList<>();
        for (int i = 0; i < inputExample.length; i++) {
            InputCell inputCell = inputCells[i];
            int input = inputExample[i];
            Synapse synapse = inputCell.getSynapse(classCell);
            if (input * synapse.getWeight() > 0) {
                eligibleSynapses.add(synapse);
            }
        }
        long kPerN = config.k() / eligibleSynapses.size();
        for (Synapse synapseToUpdate : eligibleSynapses) {
            synapseToUpdate.setWeight(synapseToUpdate.getWeight() + config.c() + kPerN);
        }
    }

    public int[] evaluate(int[] input) {
        if (input.length != numInputCells) {
            throw new IllegalArgumentException("the number of inputs does not equal the number of input cells");
        }

        int[] relativeSpikeFrequencies = new int[numClassCells];
        for (int c = 0; c < numClassCells; c++) {
            relativeSpikeFrequencies[c] = classCells[c].activate(input);
        }
        return relativeSpikeFrequencies;
    }
}
