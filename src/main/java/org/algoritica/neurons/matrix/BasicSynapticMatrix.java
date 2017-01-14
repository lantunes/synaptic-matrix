package org.algoritica.neurons.matrix;

import java.util.ArrayList;
import java.util.List;

public class BasicSynapticMatrix implements SynapticMatrix {

    private final SynapticConfig config;
    private final List<InputCell> inputCells;
    private final List<ClassCell> classCells;

    public BasicSynapticMatrix(int numInputCells, int initialNumClassCells, SynapticConfig config) {
        if (numInputCells < 1) {
            throw new IllegalArgumentException("there must be at least 1 input cell");
        }

        this.inputCells = new ArrayList<>(numInputCells);
        this.classCells = new ArrayList<>(initialNumClassCells);
        this.config = config;

        construct(numInputCells, initialNumClassCells);
    }

    private void construct(int numInputCells, int initialNumClassCells) {
        for (int i = 0; i < numInputCells; i++) {
            inputCells.add(new InputCell("#" + (i+1)));
            for (int c = 0; c < initialNumClassCells; c++) {
                if (!indexExists(classCells, c)) {
                    classCells.add(new ClassCell("#" + (c+1)));
                }
                Synapse s = new Synapse(config.b(), inputCells.get(i), classCells.get(c));
                inputCells.get(i).addAxonalSynapse(s);
                classCells.get(c).addDendriticSynapse(s);
            }
        }
    }

    private void addNewClassCell() {
        ClassCell newClassCell = new ClassCell("#" + (classCells.size() + 1));
        for (int i = 0; i < inputCells.size(); i++) {
            Synapse s = new Synapse(config.b(), inputCells.get(i), newClassCell);
            inputCells.get(i).addAxonalSynapse(s);
            newClassCell.addDendriticSynapse(s);
        }
        classCells.add(newClassCell);
    }

    @Override
    public int train(int[] inputExample) {
        addNewClassCell();
        int classCell = classCells.size() - 1;
        train(inputExample, classCell);
        return classCell;
    }

    @Override
    public void train(int[] inputExample, int classCell) {
        if (inputExample.length != inputCells.size()) {
            throw new IllegalArgumentException("the number of inputs in example " + classCell +
                    " does not equal the number of input cells");
        }

        List<Synapse> eligibleSynapses = new ArrayList<>();
        for (int i = 0; i < inputExample.length; i++) {
            InputCell inputCell = inputCells.get(i);
            int input = inputExample[i];
            Synapse synapse = inputCell.getSynapse(classCell);
            if (input * synapse.getWeight() > 0) {
                eligibleSynapses.add(synapse);
            }
        }
        if (eligibleSynapses.size() > 0) {
            long kPerN = config.k() / eligibleSynapses.size();
            for (Synapse synapseToUpdate : eligibleSynapses) {
                synapseToUpdate.setWeight(synapseToUpdate.getWeight() + config.c() + kPerN);
            }
        }
    }

    @Override
    public int[] evaluate(int[] input) {
        if (input.length != inputCells.size()) {
            throw new IllegalArgumentException("the number of inputs does not equal the number of input cells");
        }

        int[] relativeSpikeFrequencies = new int[classCells.size()];
        for (int c = 0; c < classCells.size(); c++) {
            relativeSpikeFrequencies[c] = classCells.get(c).activate(input);
        }
        return relativeSpikeFrequencies;
    }

    private boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }
}

