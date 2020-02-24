package com.grampabacon.shors;

import com.grampabacon.shors.quantum.Hadamard;
import com.grampabacon.shors.quantum.QubitRegister;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    public int findPeriod(int a, int N) {
        int bit_length = Hadamard.bitCount(N);

        int inputQubits = (2 * bit_length) - 1;
        if ((1 << inputQubits) < (N * N)) {
            inputQubits += 1;
        }

        int q = 1 << inputQubits;

        System.out.print("Finding Period...");
        System.out.print("Q = " + q + "    a = " + a);

        QubitRegister input_register = new QubitRegister(inputQubits);
        QubitRegister hadamard_input_register = new QubitRegister(inputQubits);
        QubitRegister qft_input_register = new QubitRegister(inputQubits);
        QubitRegister output_register = new QubitRegister(inputQubits);

        System.out.print("Qubit registers created.");
        System.out.print("Performing hadamard gate on input register.");

        input_register.map_hadamard(hadamard_input_register, (x) -> Hadamard.perform(x, q), false);

        System.out.print("Hadamard performed");
    }
}
