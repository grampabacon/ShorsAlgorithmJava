package com.grampabacon.shors;

import com.grampabacon.shors.classical.GreatestCommonDivisor;
import com.grampabacon.shors.classical.ModularExponentiation;
import com.grampabacon.shors.quantum.Hadamard;
import com.grampabacon.shors.quantum.PeriodFinder;
import com.grampabacon.shors.quantum.QubitRegister;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public int picka(int N) {
        int a = (int) Math.floor((new Random().nextDouble() * (N - 1)) + 0.5);
        return a;
    }

    public Integer checkCandidates(int a, Integer period, int N, int neighbourhood) {
        if (period == null) {
            return null;
        }
        for (int i = 1; i < neighbourhood + 2; i++) {
            int _r = i * period;
            if (ModularExponentiation.modularExponentiation(a, a, N) == ModularExponentiation.modularExponentiation(a, a + _r, N)) {
                return _r;
            }
        }

        for (int j = (period - neighbourhood); j < period; j++) {
            if (ModularExponentiation.modularExponentiation(a, a, N) == ModularExponentiation.modularExponentiation(a, a + j, N)) {
                return j;
            }
        }

        for (int k = (period + 1); k < (period + neighbourhood + 1); k++) {
            if (ModularExponentiation.modularExponentiation(a, a, N) == ModularExponentiation.modularExponentiation(a, a + k, N)) {
                return k;
            }
        }

        return null;
    }

    public void runShors(int N, int attempts, double neighbourhood, int numPeriods) {
        if (new BigInteger(String.valueOf(N)).bitLength() > 30 || N < 3) {
            System.out.println("Input N has too many or too few bits.");
            return;
        }

        ArrayList<Integer> periods = new ArrayList<>();
        int neighbour = (int) Math.floor(N * neighbourhood) + 1;

        System.out.println("N = " + N);
        System.out.println("Neighbourhood = " + neighbour);
        System.out.println("Number of periods to find = " + numPeriods);

        for (int attempt = 0; attempt < attempts; attempt++) {
            System.out.println("\nAttempt " + attempt);

            int a = picka(N);
            while (a < 2) {
                a = picka(N);
            }

            int divisor = GreatestCommonDivisor.greatestCommonDivisor(a, N);
            if (divisor > 1) {
                System.out.println("Found factor classically, retry.");
                continue;
            }

            Integer period = PeriodFinder.findPeriod(a, N);

            System.out.println("Checking candidate period, nearby values, and multiples");

            period = checkCandidates(a, period, N, neighbour);

            if (period == null) {
                System.out.println("No period found, retry.");
                continue;
            }

            if ((period % 2) != 0) {
                System.out.println("Period was odd, retry.");
                continue;
            }

            divisor = ModularExponentiation.modularExponentiation(a, period / 2, N);
            if (period == 0 || divisor == (N - 1)) {
                System.out.println("Period was trivial, retry.");
                continue;
            }

            System.out.println("Found period = " + period);

            periods.add(period);
            if (periods.size() < numPeriods) {
                continue;
            }

            System.out.println("\nFinding GCD of found periods.");

            period = 1;
            for (int r : periods) {
                divisor = GreatestCommonDivisor.greatestCommonDivisor(r, period);
                period = (period * r) / divisor;
            }

            int b = ModularExponentiation.modularExponentiation(a, period / 2, N);
            System.out.println("Factors: " + GreatestCommonDivisor.greatestCommonDivisor(N, b + 1) + ", " + GreatestCommonDivisor.greatestCommonDivisor(N, b - 1));
        }
        return;
    }

    public static void main(String[] args) {
	     Main main = new Main();
	     main.runShors(35, 20, 0.1, 2);
    }
}
