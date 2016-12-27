package com.algoritica.neurons.matrix;

public class Synapse {

    private final InputCell inputCell;

    private final ClassCell classCell;

    private int weight;

    public Synapse(int initialWeight, InputCell inputCell, ClassCell classCell) {
        this.weight = initialWeight;
        this.inputCell = inputCell;
        this.classCell = classCell;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String toString() {
        return "[" + inputCell + "][" + classCell + "]";
    }
}
