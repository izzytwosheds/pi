package com.twosheds.pi;

public class ChudnovskyActivity extends PiSeriesActivity {
    private double multiplier;
    private double factorial;
    private double factorial3;
    private double factorial6;
    private double piInverse;

    @Override
    protected void init() {
        multiplier = 12.0 / Math.pow(640320, 1.5);
        factorial = 1.0;
        factorial3 = 1.0;
        factorial6 = 1.0;
        piInit = 0;
        piInverse = 0.0;
        formulaView.setImageResource(R.drawable.chudnovsky);
    }

    @Override
    protected double calculatePi(double oldPi) {
        if (numSteps > 0) {
            factorial *= numSteps;
            for (int factor = (numSteps - 1) * 3 + 1; factor <= numSteps * 3; factor++) {
                factorial3 *= factor;
            }
            for (int factor = (numSteps - 1) * 6 + 1; factor <= numSteps * 6; factor++) {
                factorial6 *= factor;
            }
        }

        double term = multiplier * factorial6 * (13591409 + 545140134 * numSteps) / (factorial3 * Math.pow(factorial, 3) * Math.pow(-640320, 3 * numSteps));

        piInverse += term;
        double pi = 1.0 / piInverse;

        return pi;
    }
}
