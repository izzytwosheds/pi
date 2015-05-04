package com.twosheds.pi;

public class RamanujanActivity extends PiSeriesActivity {
    private double multiplier;
    private double factorial;
    private double factorial4;
    private double piInverse;

    @Override
    protected void init() {
        multiplier = 2.0 * Math.sqrt(2.0) / 9801.0;
        factorial = 1.0;
        factorial4 = 1.0;
        piInit = 0;
        piInverse = 0.0;
        formulaView.setImageResource(R.drawable.ramanujan);
    }

    @Override
    protected double calculatePi(double oldPi) {
        if (numSteps > 0) {
            factorial *= numSteps;
            for (int factor = (numSteps - 1) * 4 + 1; factor <= numSteps * 4; factor++) {
                factorial4 *= factor;
            }
        }

        double term = multiplier * factorial4 * (1103 + 26390 * numSteps) / (Math.pow(factorial, 4) * Math.pow(396, 4 * numSteps));

        piInverse += term;
        double pi = 1.0 / piInverse;

        return pi;
    }
}
