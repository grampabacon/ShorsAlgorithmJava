package com.grampabacon.shors.quantum;

import org.apache.commons.math3.complex.Complex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    public void propagate(QubitRegister register) {
        for (State state : this.states) {
            Complex amplitude = new Complex(0, 0);
            try {
                List<QuantumMap> maps = state.getEntangled_states().get(register);
                for (QuantumMap map : maps) {
                    amplitude = amplitude.add(map.getState().getAmplitude().multiply(map.getAmplitude()));
                }
                state.setAmplitude(amplitude);
            } catch (Exception e) {
                state.setAmplitude(amplitude);
            }
        }
        this.propagate();
    }

    public void propagate() {
        for (QubitRegister register : this.entangled) {
            if (register == null) {
                continue;
            }
            register.propagate(this);
        }
    }

    public void map_hadamard(QubitRegister register, int q, boolean propagate) {
        this.entangled.add(register);
        register.entangled.add(this);

        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_x = new HashMap();
        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_y = new HashMap();
        for (int x = 0; x < this.numberStates; x++) {
            map_tensor_x.put(x, new HashMap<>());
            List<ValueMap> codomain = Hadamard.perform(x, q);
            for (ValueMap element : codomain) {
                int y = element.getValue();
                map_tensor_x.get(x).put(y, element);
                try {
                    map_tensor_y.get(y).put(x, element);
                } catch (Exception e) {
                    map_tensor_y.put(y, (HashMap<Integer, ValueMap>) Map.of(x, element));
                }
            }
        }
        normalise(map_tensor_x);
        normalise(map_tensor_y);
        map_tensor_x.forEach((x, value) -> value.forEach((y, element) -> {
            Complex amplitude = element.getAmplitude();

            State toState = register.states.get(y);
            State fromState = this.states.get(x);

            toState.entangle(fromState, amplitude);
            fromState.entangle(toState, amplitude.conjugate());
        }));
        if (propagate) {
            register.propagate(this);
        }
    }

    public void normalise(HashMap<Integer, HashMap<Integer, ValueMap>> tensor) {
        for (HashMap<Integer, ValueMap> vector : tensor.values()) {
            double sumProb = 0.0;
            for (ValueMap element : vector.values()) {
                Complex amplitude = element.getAmplitude();
                sumProb += amplitude.multiply(amplitude.conjugate()).getReal();
            }
            double normalised = Math.sqrt(sumProb);
            for (ValueMap element : vector.values()) {
                element.setAmplitude(element.getAmplitude().divide(normalised));
            }
        }
    }
}
