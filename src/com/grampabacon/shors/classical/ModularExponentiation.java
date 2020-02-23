package com.grampabacon.shors.classical;

public class ModularExponentiation {
    public static int modularExponentiation(int a, int exp, int mod) {
        int out = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                out = out * a % mod;
            }
            a = (a * a) % mod;
            exp = exp >> 1;
        }

        return out;
    }
}
