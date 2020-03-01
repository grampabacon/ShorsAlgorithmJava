package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.Complex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class State {
    private Complex amplitude;
    private QubitRegister register;
    private HashMap<QubitRegister, List<QuantumMap>> entangledStates;

    public State(Complex amplitude, QubitRegister register) {
        this.amplitude = amplitude;
        this.register = register;
        this.entangledStates = new HashMap<>();
    }

    public void entangle(State state, Complex amplitude) {
        QubitRegister qubitRegister = state.getRegister();
        QuantumMap entanglement = new QuantumMap(state, amplitude);
        try {
            this.entangledStates.get(qubitRegister).add(entanglement);
        } catch (Exception e) {
            this.entangledStates.put(qubitRegister, Arrays.asList(entanglement));
        }
    }

    public int getEntangledStatesLength() {
        int entangled = 0;
        for (List<QuantumMap> maps : this.entangledStates.values()) {
            entangled += maps.size();
        }
        return entangled;
    }

    public int getEntangledStatesLength(QubitRegister register) {
        return this.entangledStates.get(register).size();
    }

    public void setAmplitude(Complex amplitude) {
        this.amplitude = amplitude;
    }

    public QubitRegister getRegister() {
        return register;
    }

    public HashMap<QubitRegister, List<QuantumMap>> getEntangledStates() {
        return entangledStates;
    }

    public Complex getAmplitude() {
        return amplitude;
    }
}
