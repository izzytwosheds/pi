package com.twosheds.pi;

public class VieteActivity extends PiSeriesActivity {
    private double product;

    @Override
    protected void init() {
        piInit = 1;
        product = 1.0;
        formulaView.setImageResource(R.drawable.viete);
    }

    @Override
    protected double calculatePi(double oldPi) {
        double productTerm = Math.sqrt(2);
        for (int i=0; i<numSteps; i++) {
            productTerm = Math.sqrt(2 + productTerm);
        }
        productTerm /= 2;

        product *= productTerm;

        return 2 / product;
    }
}
