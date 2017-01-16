package org.algoritica.neurons.matrix.demo;

import org.algoritica.neurons.matrix.SupervisedSynapticMatrix;
import org.algoritica.neurons.matrix.SynapticConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * b  c   k     accuracy
 * ---------------------
 * 1  20  1000  ~87%
 * 1  20  1500  89.05%
 * 1  20  2000  91.38%
 * 1  20  2500  91.67%
 * 1  20  3000  92.14%
 * 1  20  3500  92.61%
 * 1  20  3700  92.66%
 * 1  15  3900  92.63%
 * 1  15  3920  92.49%
 * 1  5   4000  91.41%
 * 1  20  4000  92.87%
 * 1  100 4000  86.02%
 * 1  20  4100  92.77%
 * 1  10  4200  92.30%
 * 1  14  4200  92.53%
 * 1  15  4150  92.51%
 * 1  15  4200  93.05% **
 * 1  15  4225  92.56%
 * 1  15  4250  92.84%
 * 1  16  4200  93.05%
 * 1  17  4200  92.89%
 * 1  18  4200  92.91%
 * 1  20  4200  92.88%
 * 1  25  4200  92.69%
 * 1  20  4300  92.42%
 * 1  20  4500  92.66%
 * 1  20  5000  92.79%
 * 1  20  6000  92.55%
 * 1  20  10000 92.42%
 * 1  20  70000 88.35%
 * With b=1, c=15, and k=4200, this synaptic matrix achieves a classification accuracy of 93.05%.
 */
public class MNISTDemoByAddingClassCells {

    private static final Logger logger = LoggerFactory.getLogger(MNISTDemoByAddingClassCells.class);

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {

        String start = df.format(new Date());

        SupervisedSynapticMatrix synapticMatrix = new SupervisedSynapticMatrix(784, new SynapticConfig(1, 15, 4200));
        MNISTDemoRunner.run(synapticMatrix);

        logger.info("start: " + start);
        logger.info("# of class cells: " + synapticMatrix.getNumClassCells());
        logger.info("end: " + df.format(new Date()));

        /*
         * when adding S into the equation:
         * b  c   k     Lim      accuracy  normalizing input during activation?
         * --------------------------------------------------------------------
         * 1  15  1500  -        87.91%    no
         * 1  20  4000  -        88.33%    no
         * 1  15  4200  -        89.73%    no
         * 1  15  4200  -        86.44%    yes^
         * 1  15  5000  -        89.08%    no
         * 1  15  4200  5000     72.49%    no
         * 1  15  4200  9000     89.01%    no
         * 1  15  4200  10000^^  89.31%    no
         * ^it appears that if we add S into the weight, we can't normalize the input during evaluation
         * ^^the highest weights seen when there is not Limit are just above 10000
         */

        /*
         * when diluting (i.e. reducing) the number of synaptic connections per class cell, randomly for each class cell
         * b  c  k     synaptic dilution  accuracy
         * ---------------------------------------
         * 1  15 4200  10%                92.11%       (~10% more class cells after training)
         * 1  15 3800  10%                92.31%       (~10% more class cells after training)
         * 1  15 4200  35%                90.58%       (~35% more class cells after training)
         * 1  15 2700  35%                90.17%       (~35% more class cells after training)
		 */

        /*
		 * when using double instead of long for the weight, accuracy is only ~86%
         */
    }
}
