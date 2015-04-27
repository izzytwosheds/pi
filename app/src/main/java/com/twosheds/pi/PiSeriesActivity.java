package com.twosheds.pi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class PiSeriesActivity extends ActionBarActivity {
    static final double ACCURACY = 0.00000000000001;
    protected static final int EVENT_NEW_VALUE = 1;
    protected static final int EVENT_FINISHED_CALCULATION = 2;

    protected static int numSteps;
    protected static boolean isRunning;

    protected TextView piView;
    protected ImageView transparentPiView;
    protected ImageView formulaView;
    protected TextView stepView;
    protected Button startButton;

    protected double piInit;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            int event = message.what;
            switch (event) {
                case EVENT_NEW_VALUE:
                    updateViews((double) message.obj);
                    break;
                case EVENT_FINISHED_CALCULATION:
                    stopCalculation();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        piView = (TextView) findViewById(R.id.pi);
        transparentPiView = (ImageView) findViewById(R.id.pi_transparent);
        formulaView = (ImageView) findViewById(R.id.formula);
        stepView = (TextView) findViewById(R.id.steps);
        startButton = (Button) findViewById(R.id.button_start);

        numSteps = 0;
        updateViews(0.0);

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    protected void init() {
        piInit = 0;
    }

    protected abstract double calculatePi(double oldPi);

    public void onStartCalculation(View view) {
        if (isRunning) {
            stopCalculation();
        } else {
            isRunning = true;
            startButton.setText(R.string.action_stop);
            Thread drawThread = new Thread() {
                @Override
                public void run() {
                    double oldPi = 10;
                    double pi = piInit;

                    while (isRunning && Math.abs(oldPi - pi) > ACCURACY) {
                        oldPi = pi;
                        pi = calculatePi(pi);
                        numSteps++;

                        Message msg = handler.obtainMessage(EVENT_NEW_VALUE);
                        msg.obj = pi;
                        msg.sendToTarget();

                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.obtainMessage(EVENT_FINISHED_CALCULATION).sendToTarget();
                }
            };
            drawThread.start();
        }
    }

    protected void updateViews(double pi) {
        piView.setText(getString(R.string.pi, pi));
        stepView.setText(getString(R.string.iteration, numSteps));
        transparentPiView.setAlpha(getAlpha(pi));
    }

    protected void stopCalculation() {
        isRunning = false;
        startButton.setText(R.string.action_start);
        numSteps = 0;
        init();
    }

    protected float getAlpha(double pi) {
        double accuracy = Math.min(1.0, Math.abs(Math.PI - pi));
        double accuracyFactor = Math.log10(PiSeriesActivity.ACCURACY / accuracy) / Math.log10(PiSeriesActivity.ACCURACY);
        return 1 - Math.min(1, (float) (1 * accuracyFactor));
    }
}
