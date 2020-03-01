package com.grampabacon.shors.classical;

import java.util.ArrayList;

public class GreatestCommonDivisor {
    public static int greatestCommonDivisor(int a, int b) {
        while (b > 0) {
            int _t = a % b;
            a = b;
            b = _t;
        }

        return a;
    }

    public static ArrayList<Integer> extendedGCD(int a, int b) {
        ArrayList<Integer> fractions = new ArrayList<>();
        while (b != 0) {
            fractions.add(a / b);

            int _t = a % b;
            a = b;
            b = _t;
        }

        return fractions;
    }
}
