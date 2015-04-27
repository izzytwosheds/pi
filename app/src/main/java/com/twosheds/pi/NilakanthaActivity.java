package com.twosheds.pi;

public class NilakanthaActivity extends PiSeriesActivity {
    private double d;
    private int sign;

    @Override
    protected void init() {
        d = 2.0;
        sign = 1;
        piInit = 3;
        formulaView.setImageResource(R.drawable.nilakantha);
    }

    @Override
    protected double calculatePi(double oldPi) {
        double pi = oldPi + 4.0 * sign / (d * (d+1.0) * (d+2.0));

        d += 2.0;
        sign = -1 * sign;

        return pi;
    }
}
