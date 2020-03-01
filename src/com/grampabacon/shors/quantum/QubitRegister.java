package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.Complex;

import java.util.*;

public class QubitRegister {
    final String name;

    int numberBits = 0;
    int numberStates = 0;
    ArrayList<QubitRegister> entangled;
    ArrayList<State> states;

    public QubitRegister(String name, int numberBits) {
        this.name = name;

        this.numberBits = numberBits;
        this.numberStates = 1 << numberBits;

        this.entangled = new ArrayList<>();
        this.states = new ArrayList<>();
        for (int i = 0; i < this.numberStates; i++) {
            this.states.add(new State(new Complex(0.0), this));
        }

        this.states.get(0).setAmplitude(new Complex(1.0));
    }

    public void propagate(QubitRegister register) {
        if (register != null) {
            List<QuantumMap> maps;
            Complex amplitude;
            for (State state : this.states) {
                amplitude = new Complex(0.0);
                try {
                    maps = state.getEntangledStates().get(register);
                    for (QuantumMap map : maps) {
                        amplitude = amplitude.add(map.getState().getAmplitude().multiply(map.getAmplitude()));
                    }
                } catch (Exception ignored) {

                }
                state.setAmplitude(amplitude);
            }
        }
        for (QubitRegister qr : this.entangled) {
            if (qr.equals(register)) {
                continue;
            }
            qr.propagate(this);
        }
    }

    public void mapHadamard(QubitRegister register, int q, boolean propagate) {
        this.entangled.add(register);
        register.entangled.add(this);

        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_x = new HashMap<>();
        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_y = new HashMap<>();
        for (int x = 0; x < this.numberStates; x++) {
            map_tensor_x.put(x, new HashMap<>());
            List<ValueMap> codomain = Hadamard.perform(x, q);
            for (ValueMap element : codomain) {
                int y = element.getValue();
                map_tensor_x.get(x).put(y, element);
                try {
                    map_tensor_y.get(y).put(x, element);
                } catch (Exception e) {
                    HashMap<Integer, ValueMap> map = new HashMap<>();
                    map.put(x, element);
                    map_tensor_y.put(y, map);
                }
            }
        }
        normalise(map_tensor_x);
        normalise(map_tensor_y);

        /*for (int i = 0; i < map_tensor_x.size(); i++) {
            for (int j = 0; j < map_tensor_y.size(); j++) {
                Complex amplitude = map_tensor_x.get(i).get(j).getAmplitude();

                State toState = register.states.get(j);
                State fromState = this.states.get(i);

                toState.entangle(fromState, amplitude);
                fromState.entangle(toState, amplitude.conjugate());
            }
        }*/

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

    public void mapQft(QubitRegister register, int q, boolean propagate) {
        this.entangled.add(register);
        register.entangled.add(this);

        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_x = new HashMap<>();
        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_y = new HashMap<>();
        for (int x = 0; x < this.numberStates; x++) {
            map_tensor_x.put(x, new HashMap<>());
            List<ValueMap> codomain = QuantumFourierTransform.perform(x, q);
            for (ValueMap element : codomain) {
                int y = element.getValue();
                map_tensor_x.get(x).put(y, element);
                try {
                    map_tensor_y.get(y).put(x, element);
                } catch (Exception e) {
                    HashMap<Integer, ValueMap> map = new HashMap<>();
                    map.put(x, element);
                    map_tensor_y.put(y, map);
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

    public void mapQuantumModularExponentiation(QubitRegister register, int a, int N, boolean propagate) {
        this.entangled.add(register);
        register.entangled.add(this);

        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_x = new HashMap();
        HashMap<Integer, HashMap<Integer, ValueMap>> map_tensor_y = new HashMap();
        for (int x = 0; x < this.numberStates; x++) {
            map_tensor_x.put(x, new HashMap<>());
            List<ValueMap> codomain = QuantumModularExponentiation.perform(a, x, N);
            for (ValueMap element : codomain) {
                int y = element.getValue();
                map_tensor_x.get(x).put(y, element);
                try {
                    map_tensor_y.get(y).put(x, element);
                } catch (Exception e) {
                    HashMap<Integer, ValueMap> map = new HashMap<>();
                    map.put(x, element);
                    map_tensor_y.put(y, map);
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
                sumProb += amplitude.multiply(amplitude.conjugate()).real();
            }
            double normalised = Math.sqrt(sumProb);
            for (ValueMap element : vector.values()) {
                element.setAmplitude(element.getAmplitude().scale(1 / normalised));
            }
        }
    }

    public double measure() {
        double measure = (new Random()).nextDouble();
        double sumProb = 0.0;

        int finalValue = 0;
        State finalState = null;
        for (int i = 0; i < this.states.size(); i++) {
            State state = states.get(i);
            Complex amplitude = state.getAmplitude();
            sumProb += amplitude.multiply(amplitude.conjugate()).real();

            if (sumProb > measure) {
                finalState = state;
                finalValue = i;
                break;
            }
        }
        if (finalState != null) {
            for (State state : states) {
                state.setAmplitude(new Complex(0.0));
            }
            finalState.setAmplitude(new Complex(1.0));
            this.propagate(null);
        }
        return finalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QubitRegister qr = (QubitRegister) o;
        return numberBits == qr.numberBits &&
                numberStates == qr.numberStates &&
                entangled == (qr.entangled) &&
                states.equals(qr.states);
    }
}
