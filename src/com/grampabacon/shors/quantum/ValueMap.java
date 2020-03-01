package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.Complex;

public class ValueMap {
    private int value;
    private Complex amplitude;

    public ValueMap(int value, Complex amplitude) {
        this.value = value;
        this.amplitude = amplitude;
    }

    public Complex getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(Complex amplitude) {
        this.amplitude = amplitude;
    }

    public int getValue() {
        return value;
    }
}
