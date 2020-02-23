package com.grampabacon.shors.quantum;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;

public class QubitRegister {
    int numberBits = 0;
    int numberStates = 0;
    ArrayList<QubitRegister> entangled;
    ArrayList<State> states;

    public QubitRegister(int numberBits) {
        this.numberBits = numberBits;
        this.numberStates = 1 << numberBits;

        this.entangled = new ArrayList<QubitRegister>();
        this.states = new ArrayList<State>();
        for (int i = 0; i < this.numberStates; i++) {
            this.states.add(new State(new Complex(0.0), this));
        }

        this.states.get(0).setAmplitude(new Complex(1));
    }
}
