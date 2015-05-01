package com.twosheds.pi;

public class GaussLegendreActivity extends PiSeriesActivity {
    private double a;
    private double b;
    private double t;
    private double p;

    @Override
    protected void init() {
        a = 1.0;
        b = 1.0 / Math.sqrt(2);
        t = 1.0 / 4.0;
        p = 1.0;
        formulaView.setImageResource(R.drawable.gauss_legendre);
    }

    @Override
    protected double calculatePi(double oldPi) {
        double pi = (a + b) * (a + b) / (4 * t);

        double newA = (a + b) / 2;
        double newB = Math.sqrt(a * b);
        double newT = t - p * (a - newA) * (a - newA);
        double newP = 2 * p;

        a = newA;
        b = newB;
        t = newT;
        p = newP;

        return pi;
    }
}
