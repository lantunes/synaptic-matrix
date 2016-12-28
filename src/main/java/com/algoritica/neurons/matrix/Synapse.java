package com.algoritica.neurons.matrix;

public class Synapse {

    private final InputCell inputCell;

    private final ClassCell classCell;

    private long weight;

    public Synapse(long initialWeight, InputCell inputCell, ClassCell classCell) {
        this.weight = initialWeight;
        this.inputCell = inputCell;
        this.classCell = classCell;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public String toString() {
        return "[" + inputCell + "][" + classCell + "]";
    }
}
