package org.algoritica.neurons.matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MatrixUtils {

    public static int[] toLinearArray(int[][] arr) {
        int[] linearArray = new int[arr.length * arr[0].length];
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                linearArray[pos++] = arr[i][k];
            }
        }
        return linearArray;
    }

    public static void serialize(SynapticMatrix matrix, File file) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
            writer.write(matrix.toJSON().toString(2));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends SynapticMatrix> T deserialize(Class<T> type, File file) throws Exception {
        return null;
    }
}
