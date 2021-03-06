package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.Complex;

import java.util.ArrayList;
import java.util.List;

public class Hadamard {
    public static int bitCount(int N) {
        int sum = 0;
        while (N > 0) {
            sum += N & 1;
            N >>= 1;
        }
        return sum;
    }

    public static List<ValueMap> perform(int N, int Q) {
        List<ValueMap> codomain = new ArrayList<>();
        for (int i = 0; i < Q; i++) {
            codomain.add(new ValueMap(i, new Complex(Math.pow(-1.0, bitCount(N & i) & 1))));
        }
        return codomain;
    }
}
