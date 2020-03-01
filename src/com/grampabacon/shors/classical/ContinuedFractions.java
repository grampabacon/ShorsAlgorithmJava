package com.grampabacon.shors.classical;

import java.util.ArrayList;

public class ContinuedFractions {
    public static double continuedFractions(int y, int Q, int N) {
        ArrayList<Integer> fractions = GreatestCommonDivisor.extendedGCD(y, Q);
        int depth = 2;

        int rCF = 0;
        for (int i = depth; i <= fractions.size() - 1; i++) {
            int _r = partial(fractions, i);
            if (_r == rCF || _r >= N) {
                return rCF;
            }
            rCF = _r;
        }

        return rCF;
    }

    public static int partial(ArrayList<Integer> fractions, int depth) {
        int c = 0;
        int r = 1;

        for (int i = depth - 1; i >= 0; i--) {
            int _c = fractions.get(i) * r + c;
            c = r;
            r = _c;
        }

        return c;
    }
}
