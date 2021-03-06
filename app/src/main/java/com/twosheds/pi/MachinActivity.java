package com.twosheds.pi;

public class MachinActivity extends PiSeriesActivity {
    private double d;
    private int sign;

    private double z1 = 1.0 / 5.0;
    private double z2 = 1.0 / 239.0;

    @Override
    protected void init() {
        formulaView.setImageResource(R.drawable.machin);
        d = 1;
        sign = 1;
    }

    @Override
    protected double calculatePi(double oldPi) {
        double pi = oldPi + 4.0 * sign * (4 * Math.pow(z1, d) / d - Math.pow(z2 , d) / d);

        d += 2.0;
        sign = -1 * sign;

        return pi;
    }
}
