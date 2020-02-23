package com.grampabacon.shors.quantum;

import org.apache.commons.math3.complex.Complex;

public class QuantumMap {
    private State state;
    private Complex amplitude;

    public QuantumMap(State state, Complex amplitude) {
        this.state = state;
        this.amplitude = amplitude;
    }

    public Complex getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(Complex amplitude) {
        this.amplitude = amplitude;
    }

    public State getState() {
        return state;
    }
}
