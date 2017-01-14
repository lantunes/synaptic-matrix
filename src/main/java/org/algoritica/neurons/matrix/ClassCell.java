package org.algoritica.neurons.matrix;

import java.util.ArrayList;
import java.util.List;

public class ClassCell {

    private final String id;

    private final List<Synapse> dendriticSynapses = new ArrayList<>();

    public ClassCell(String id) {
        this.id = id;
    }

    public void addDendriticSynapse(Synapse synapse) {
        dendriticSynapses.add(synapse);
    }

    public int activate(int[] inputs) {
        if (inputs.length != dendriticSynapses.size()) {
            throw new IllegalArgumentException("the number of inputs does not match the number of dendritic synapses");
        }
        int activation = 0;
        for (int i = 0; i < inputs.length; i++) {
            int input = inputs[i] > 1 ? 1 : inputs[i]; //TODO normalize inputs to 1 for now
            activation += input * dendriticSynapses.get(i).getWeight();
        }
        return activation;
    }

    public String toString() {
        return "ClassCell " + id;
    }
}
