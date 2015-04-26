package com.twosheds.pi;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MonteCarloActivity extends ActionBarActivity {
    static final double ACCURACY = 0.00000000000001;
    private static final int EVENT_NEW_VALUE = 1;
    private static final int EVENT_FINISHED_CALCULATION = 2;

    private Random random;

    private static int countInside;
    private static int countTotal;
    private static boolean isRunning;

    private GraphView graphView;
    private TextView piView;
    private ImageView transparentPiView;
    private TextView stepView;
    private Button startButton;

    private Handler handler = new Handler() {
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
        setContentView(R.layout.activity_monte_carlo);

        graphView = (GraphView) findViewById(R.id.graph);
        piView = (TextView) findViewById(R.id.pi);
        transparentPiView = (ImageView) findViewById(R.id.pi_transparent);
        stepView = (TextView) findViewById(R.id.steps);
        startButton = (Button) findViewById(R.id.button_start);

        countTotal = 0;
        countInside = 0;

        random = new Random();
        updateViews(0.0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    public void onStartCalculation(View view) {
        if (isRunning) {
            stopCalculation();
        } else {
            isRunning = true;
            startButton.setText(R.string.action_stop);
            Thread drawThread = new Thread() {
                @Override
                public void run() {
                    double oldPi = 10.0;
                    double pi = 20.0;
                    graphView.clearPoints();
                    while (isRunning && Math.abs(oldPi - pi) > ACCURACY) {
                        double x = random.nextDouble() * 2.0d - 1.0d;
                        double y = random.nextDouble() * 2.0d - 1.0d;
                        boolean isInside  = (x * x + y * y) < 1.0d;
                        if (isInside) {
                            countInside++;
                        }
                        countTotal++;

                        if (countTotal != countInside && countInside > 0) {
                            oldPi = pi;
                        }
                        pi = (double) countInside * 4.0d / (double) countTotal;

                        Message msg = handler.obtainMessage(EVENT_NEW_VALUE);
                        msg.obj = pi;
                        msg.sendToTarget();
                        graphView.drawPoint(x, y, isInside);
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

    private void updateViews(double pi) {
        piView.setText(getString(R.string.pi, pi));
        stepView.setText(getString(R.string.iteration, countTotal));
        transparentPiView.setAlpha(getAlpha(pi));
    }

    private void stopCalculation() {
        isRunning = false;
        startButton.setText(R.string.action_start);
        countTotal = 0;
        countInside = 0;
    }

    private float getAlpha(double pi) {
        double accuracy = Math.min(1.0, Math.abs(Math.PI - pi));
        double accuracyFactor = Math.log10(MonteCarloActivity.ACCURACY / accuracy) / Math.log10(MonteCarloActivity.ACCURACY);
        return 1 - Math.min(1, (float) (1 * accuracyFactor));
    }
}
