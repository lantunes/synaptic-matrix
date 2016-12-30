package com.algoritica.neurons.matrix;

import com.algoritica.neurons.mnist.MnistManager;
import com.google.common.io.Resources;

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
}
