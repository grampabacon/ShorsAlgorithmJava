package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.Complex;

import java.util.ArrayList;
import java.util.List;

public class QuantumFourierTransform {

    public static List<ValueMap> perform(int N, int Q) {
        double dQ = (float) Q;
        double k = -2.0 * Math.PI;

        List<ValueMap> codomain = new ArrayList<>();

        for (int i = 0; i < Q; i++) {
            double angle = (k * ((float) (N * i) % Q)) / dQ;
            Complex amplitude = new Complex(Math.cos(angle), Math.sin(angle));
            codomain.add(new ValueMap(i, amplitude));
        }
        return codomain;
    }
}
