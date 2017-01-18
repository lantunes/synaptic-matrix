package org.algoritica.neurons.matrix;

import java.util.Optional;

public class Synapse {

    private final String inputCell;

    private final String classCell;

    private long weight;

    private Synapse previous;

    private Synapse next;

    public Synapse(long initialWeight, String inputCell, String classCell) {
        this.weight = initialWeight;
        this.inputCell = inputCell;
        this.classCell = classCell;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight, long limit) {
        if (weight > limit) {
            weight = limit;
        }
        this.weight = weight;
    }

    public void setPrevious(Synapse previous) {
        this.previous = previous;
    }

    public void setNext(Synapse next) {
        this.next = next;
    }

    public Optional<Synapse> previous() {
        return Optional.ofNullable(previous);
    }

    public Optional<Synapse> next() {
        return Optional.ofNullable(next);
    }

    public String toString() {
        return "[" + inputCell + "][" + classCell + "]";
    }
}
