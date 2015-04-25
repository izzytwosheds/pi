package com.twosheds.pi;

public class NilakanthaActivity extends PiSeriesActivity {
    private double d = 2.0;
    private int sign = 1;

    @Override
    protected void init() {
        piInit = 3;
        formulaView.setImageResource(R.drawable.nilakantha);
    }

    @Override
    protected double calculateTerm() {
        double term = 4.0 * sign / (d * (d+1.0) * (d+2.0));

        d += 2.0;
        sign = -1 * sign;

        return term;
    }
}
