package org.algoritica.neurons.matrix.demo;

import org.algoritica.neurons.matrix.BasicSynapticMatrix;
import org.algoritica.neurons.matrix.MatrixUtils;
import org.algoritica.neurons.matrix.SynapticConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * With b=1, c=20, and k=1500, this synaptic matrix achieves a classification accuracy of 71.19%.
 * The images in the MNIST data set are not black and white--they contain grey levels
 * (i.e. instead of 0 and 255, it contains a range of values from 0-255). However, the synaptic matrix algorithm
 * currently ignores the absolute values of the input, and treats all input values as binary
 * (e.g. 0->0, 253->1, 15->1, etc.). This *may* make it  more difficult for the matrix to differentiate certain characters.
 * -- using b=1, c=15, k=4200 results in 61.94%
 *
 * One thing to try would be to simply use the value of the input instead of k/N when updating the weights, and get rid of c.
 * -- this was tried and resulted in a 62.04% accuracy
 *
 * Another thing to try would be to perhaps try to stack another synaptic matrix or two together,
 * and create a "deep" synaptic matrix. We can try to create a kind of convolutional synaptic matrix. We can create a number of
 * synaptic matrices of smaller size that correspond to smaller, overlapping regions of the image. These matrices feed
 * up to a master matrix.
 *
 * Part of the problem may be that the digits are not scaled and/or centered the same way across the examples.
 * Indeed, Trehub uses a retinoid that attempts to translate the example to the normal foveal axis before learning.
 * However, this was on the web:
 *   '...for the MNIST digit classification problem we've been studying, the images are centered
 *    and size-normalized. So MNIST has less translation invariance than images found "in the wild", so
 *    to speak. Still, features like edges and corners are likely to be useful across much of the input space.'
 *
 * Another issue may be that there are not an even number of examples for each class. And since the weights aren't
 * normalized, some patterns tend to have just that much more weight by virtue of the fact that they are relatively,
 * and slightly, over-represented.
 *
 * Using 600,000 training examples (from the InfiniMNIST data set) resulted in 68.68% accuracy.
 */
public class MNISTDemoNormativePrototype {

    private static final Logger logger = LoggerFactory.getLogger(MNISTDemoNormativePrototype.class);

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {

        String start = df.format(new Date());

        BasicSynapticMatrix synapticMatrix = new BasicSynapticMatrix(784, 10, new SynapticConfig(1, 20, 1500));
        MNISTDemoRunner.run(synapticMatrix);

        logger.info("start: " + start);
        logger.info("end: " + df.format(new Date()));

        MatrixUtils.serialize(synapticMatrix, new File("target/matrix-mnist.json"));
    }
}
