package com.grampabacon.shors.quantum;

import com.grampabacon.shors.classical.ContinuedFractions;

import java.math.BigInteger;

public class PeriodFinder {
    public static int findPeriod(int a, int N) {
        int numBits = new BigInteger(String.valueOf(N)).bitLength();

        int inputQubits = (2 * numBits) - 1;
        if ((1 << inputQubits) < (N * N)) {
            inputQubits += 1;
        }

        int Q = 1 << inputQubits;

        System.out.println("Finding period...");
        System.out.println("Q = " + Q + "\na = " + a);

        QubitRegister inputRegister = new QubitRegister("Input", inputQubits);
        QubitRegister hadamardInputRegister = new QubitRegister("Hadamard", inputQubits);
        QubitRegister QFTInputRegister = new QubitRegister("QFT", inputQubits);
        QubitRegister outputRegister = new QubitRegister("Output", inputQubits);

        System.out.println("Qubit registers created.");
        System.out.println("Performing Hadamard gate on input register.");

        inputRegister.mapHadamard(hadamardInputRegister, Q, false);

        System.out.println("Hadamard performed.");
        System.out.println("Performing modular exponentiation.");

        hadamardInputRegister.mapQuantumModularExponentiation(outputRegister, a, N, false);

        System.out.println("Modular exponentiation performed.");
        System.out.println("Performing QFT.");

        hadamardInputRegister.mapQft(QFTInputRegister, Q, false);
        inputRegister.propagate(null);

        System.out.println("Performed QFT.");
        System.out.println("Measuring output qubit register.");

        double y = outputRegister.measure();

        System.out.println("Output register measurement = " + y);
        System.out.println("Measuring QFT register.");

        double x = QFTInputRegister.measure();

        System.out.println("QFT register measurement = " + x);
        System.out.println("Using continued fractions to find the period");

        double period = ContinuedFractions.continuedFractions((int) x, Q, N);

        System.out.println("Found period = " + period);

        return (int) period;
    }
}
