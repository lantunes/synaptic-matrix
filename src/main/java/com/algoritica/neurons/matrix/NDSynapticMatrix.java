package com.algoritica.neurons.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.conditions.GreaterThan;
import org.nd4j.linalg.util.ArrayUtil;

/*
 * An attempt at improving synaptic matrix performance by using a high-performance matrix library.
 * Performance appears worse than the BasicSynapticMatrix, and there are problems coming up with the
 * correct frequencies, as of Jan. 1 2017.
 */
public class NDSynapticMatrix implements SimpleSynapticMatrix {

    private final SynapticConfig config;

    private final INDArray matrix;

    private int nextUnusedClassCell;

    public NDSynapticMatrix(int numInputCells, int initialNumClassCells, SynapticConfig config) {

        this.config = config;
        this.matrix = Nd4j.create(numInputCells, initialNumClassCells);
        this.matrix.assign(config.b());
        this.nextUnusedClassCell = 0;
    }

   @Override
    public int train(int[] inputExample) {
        train(inputExample, nextUnusedClassCell);
        return nextUnusedClassCell++;
    }

    @Override
    public void train(int[] inputExample, int classCell) {
        if (inputExample.length != matrix.rows()) {
            throw new IllegalArgumentException("the number of inputs in example " + classCell +
                    " does not equal the number of input cells");
        }

        INDArray inputExampleColumn = Nd4j.create(ArrayUtil.toDouble(inputExample), new int[]{inputExample.length, 1});
        //this assumes the input is an array containing only 1s and 0s
        int numEligibleSynapses = inputExampleColumn.sumNumber().intValue();
        long kPerN = config.k() / numEligibleSynapses;
        long c = config.c();

        INDArray cArr = Nd4j.create(inputExample.length, 1);
        cArr.assign(c);

        INDArray kPerNArr = Nd4j.create(inputExample.length, 1);
        kPerNArr.assign(kPerN);

        INDArray cArrMul = inputExampleColumn.mul(cArr);
        INDArray kPerNArrMul = inputExampleColumn.mul(kPerNArr);

        matrix.getColumn(classCell).addi(cArrMul).addi(kPerNArrMul);
    }

    @Override
    public int[] evaluate(int[] input) {
        if (input.length != matrix.rows()) {
            throw new IllegalArgumentException("the number of inputs does not equal the number of input cells");
        }

        INDArray evalColumn = Nd4j.create(ArrayUtil.toDouble(input), new int[]{input.length,1});
        //normalize input
        evalColumn.condi(new GreaterThan(0));

        int numColumns = nextUnusedClassCell > 0 ? nextUnusedClassCell : matrix.columns();
        int[] relativeSpikeFrequencies = new int[numColumns];
        for (int i = 0; i < numColumns; i++) {
            INDArray resultCol = evalColumn.mul(matrix.getColumn(i));
            relativeSpikeFrequencies[i] = resultCol.sumNumber().intValue();
        }

        return relativeSpikeFrequencies;
    }
}
