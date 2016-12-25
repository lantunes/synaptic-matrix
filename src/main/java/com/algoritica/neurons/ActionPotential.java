package com.algoritica.neurons;

public class ActionPotential {

    private final int value;

    public ActionPotential(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
