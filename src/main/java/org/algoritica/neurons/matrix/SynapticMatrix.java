package org.algoritica.neurons.matrix;

import org.json.JSONObject;

/**
 * A synaptic matrix based on the work of Arnold Trehub.
 */
public interface SynapticMatrix<T extends SynapticMatrix> {

    /**
     * Adds a new class cell to the end of the list of class cells, trained on the input example.
     * Returns the index of the newly added class cell.
     * The inputExample must be an array containing only 0s and 1s.
     */
    int train(int[] inputExample);

    /**
     * The inputExample must be an array containing only 0s and 1s.
     */
    void train(int[] inputExample, int classCell);

    /**
     * The input must be an array containing only 0s and 1s.
     */
    int[] evaluate(int[] input);

    JSONObject toJSON();

    T fromJSON(JSONObject serializationFormat);
}
