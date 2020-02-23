package com.grampabacon.shors.classical;

public class FastPower {
    public static int fastPower(int a, int b, int N) {
        int answer = 1;
        while (b > 0) {
            if (b % 2 == 0) {
                answer = answer * a % N;
            }

            b = b / 2;
            a = a * a;
            if (answer > N) {
                return answer;
            }
        }

        return answer;
    }

    public static int fastPowerBoolean(int a, int b, int N) {
        int answer = 1;
        while (b > 0) {
            if (b % 2 == 0) {
                answer = answer * a;
            }

            b = b / 2;
            a = a * a;
            if (answer > N) {
                return answer;
            }
        }

        return answer;
    }
}
