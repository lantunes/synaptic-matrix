package org.algoritica.neurons.matrix;

public class ActivityAndClass {

    private final int activity;

    private final int classCellNumber;

    public ActivityAndClass(int activity, int classCellNumber) {
        this.activity = activity;
        this.classCellNumber = classCellNumber;
    }

    public int getActivity() {
        return activity;
    }

    public int getClassCellNumber() {
        return classCellNumber;
    }
}
