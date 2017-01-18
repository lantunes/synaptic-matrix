package org.algoritica.neurons.matrix.demo;

import org.algoritica.neurons.matrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SerializationDemo {

    private static final Logger logger = LoggerFactory.getLogger(SerializationDemo.class);

    public static void main(String[] args) throws Exception {

        SupervisedSynapticMatrix matrix = new SupervisedSynapticMatrix(2, new SynapticConfig(1, 2, 18));
        matrix.train(new int[]{1,0}, 0);
        matrix.train(new int[]{1,1}, 1);

        MatrixUtils.serialize(matrix, new File("target/matrix.json"));

//        matrix = MatrixUtils.deserialize(SupervisedSynapticMatrix.class, new File("target/matrix.xml"));
//
//        logger.info("# of class cells in deserialized matrix: " + matrix.getNumClassCells());
    }
}
