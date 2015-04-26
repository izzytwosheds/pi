package com.twosheds.pi;

public class LeinbnitzActivity extends PiSeriesActivity {
    private double denominator = 1;
    private int sign = 1;

    @Override
    protected void init() {
        formulaView.setImageResource(R.drawable.leibnitz);
    }

    @Override
    protected double calculatePi(double oldPi) {
        double pi = oldPi + 4.0 * sign / denominator;

        denominator += 2.0;
        sign = -1 * sign;

        return pi;
    }
}
