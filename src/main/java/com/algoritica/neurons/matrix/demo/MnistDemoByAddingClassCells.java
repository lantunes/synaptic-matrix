package com.algoritica.neurons.matrix.demo;

import com.algoritica.neurons.matrix.SupervisedSynapticMatrix;
import com.algoritica.neurons.matrix.SynapticConfig;

/*
 * This demo emulates the way Trehub classified hand-written digits, as described in
 * chapter 11 of The Cognitive Brain. MNIST images are centered and size-normalized, so there is not so
 * much invariance between examples as one would find in practice. Thus, the retinoid/centroid system he
 * uses before sending the image to the synaptic matrix is not so critical here.
 *
 * Outline of procedure:
 * 1. get the next example
 * 2. test it against the network
 *   - if it is correct, go to the next example
 *   - if it is incorrect, train with the example on the next available class cell
 * ** instead of creating a new class cell each time, another way would be to simply initialize the matrix
 *    with as many class cells as there are examples, and use the next one available
 *
 * If we simply accumulated the weights in the synapses of the same class cell for each digit, then
 * we would end up with what Trehub calls some "normative prototype" (see pages 199-200 of The Cognitive Brain).
 * He claims the procedure described in this demo is superior.
 *
 * With b=1, c=20, and k=1500, this synaptic matrix achieves a classification accuracy of 89.05%.
 * Might this be improved by using a retinoid/centroid, and/or by taking into account the grayscale values?
 */
public class MnistDemoByAddingClassCells {

    public static void main(String[] args) throws Exception {

        MnistDemoRunner.run(new SupervisedSynapticMatrix(784, new SynapticConfig(1, 20, 1500)));
    }
}
