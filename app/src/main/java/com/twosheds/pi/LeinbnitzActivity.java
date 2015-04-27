package com.twosheds.pi;

public class LeinbnitzActivity extends PiSeriesActivity {
    private double denominator;
    private int sign;

    @Override
    protected void init() {
        formulaView.setImageResource(R.drawable.leibnitz);
        denominator = 1;
        sign = 1;
    }

    @Override
    protected double calculatePi(double oldPi) {
        double pi = oldPi + 4.0 * sign / denominator;

        denominator += 2.0;
        sign = -1 * sign;

        return pi;
    }
}
