package org.algoritica.neurons.matrix;

import java.util.ArrayList;
import java.util.List;

public class InputCell {

    private final String id;

    private final List<Synapse> axonalSynapses = new ArrayList<>();

    public InputCell(String id) {
        this.id = id;
    }

    public void addAxonalSynapse(Synapse synapse) {
        axonalSynapses.add(synapse);
    }

    public Synapse getSynapse(int i) {
        return axonalSynapses.get(i);
    }

    public String toString() {
        return "InputCell " + id;
    }
}
