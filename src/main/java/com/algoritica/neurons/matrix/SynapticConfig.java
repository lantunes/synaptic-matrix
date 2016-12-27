package com.algoritica.neurons.matrix;

/*
 * based on the equation: weight = b + c + kN^-1
 */
public class SynapticConfig {
    private final int b;
    private final int c;
    private final int k;

    public SynapticConfig(int b, int c, int k) {
        this.b = b;
        this.c = c;
        this.k = k;
    }

    public int b() {
        return b;
    }

    public int c() {
        return c;
    }

    public int k() {
        return k;
    }
}
