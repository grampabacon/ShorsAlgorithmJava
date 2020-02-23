package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.ModularExponentiation;
import org.apache.commons.math3.complex.Complex;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class QuantumModularExponentiation {
    public static List<ValueMap> quantumModularExponentiation(int a, int exp, int mod) {
        int value = ModularExponentiation.modularExponentiation(a, exp, mod);
        return Arrays.asList(new ValueMap(value, new Complex(1.0)));
    }
}
