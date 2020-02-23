package com.grampabacon.shors.quantum;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class State {
    private Complex amplitude;
    private QubitRegister register;
    private HashMap<QubitRegister, List<QuantumMap>> entangled_states;

    public State(Complex amplitude, QubitRegister register) {
        this.amplitude = amplitude;
        this.register = register;
        this.entangled_states = new HashMap<QubitRegister, List<QuantumMap>>();
    }

    public void entangle(State state, Complex amplitude) {
        QubitRegister qubitRegister = state.getRegister();
        QuantumMap entanglement = new QuantumMap(state, amplitude);
        try {
            this.entangled_states.get(qubitRegister).add(entanglement);
        } catch (Exception e) {
            this.entangled_states.put(qubitRegister, Arrays.asList(entanglement));
        }
    }

    public int getEntangledStatesLength() {
        int entangled = 0;
        for (List<QuantumMap> maps : this.entangled_states.values()) {
            entangled += maps.size();
        }
        return entangled;
    }

    public int getEntangledStatesLength(QubitRegister register) {
        return this.entangled_states.get(register).size();
    }

    public void setAmplitude(Complex amplitude) {
        this.amplitude = amplitude;
    }

    public QubitRegister getRegister() {
        return register;
    }
}
