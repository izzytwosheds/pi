package com.twosheds.pi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class LeinbnitzActivity extends ActionBarActivity {
    static final double ACCURACY = 0.00000000000001;
    private static final int EVENT_NEW_VALUE = 1;
    private static final int EVENT_FINISHED_CALCULATION = 2;

    private static int numSteps;
    private static boolean isRunning;

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
        setContentView(R.layout.activity_leibnitz);

        piView = (TextView) findViewById(R.id.pi);
        transparentPiView = (ImageView) findViewById(R.id.pi_transparent);
        stepView = (TextView) findViewById(R.id.steps);
        startButton = (Button) findViewById(R.id.button_start);

        numSteps = 0;
        updateViews(0.0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    double oldPi = 10;
                    double pi = 0;
                    double denominator = 1;
                    int sign = 1;
                    while (isRunning && Math.abs(oldPi - pi) > ACCURACY) {
                        oldPi = pi;
                        pi += 4.0 * sign / denominator;

                        denominator += 2.0;
                        sign = -1 * sign;
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

    private void updateViews(double pi) {
        piView.setText(getString(R.string.pi, pi));
        stepView.setText(getString(R.string.iteration, numSteps));
        transparentPiView.setAlpha(getAlpha(pi));
    }

    private void stopCalculation() {
        isRunning = false;
        startButton.setText(R.string.action_start);
        numSteps = 0;
    }

    private float getAlpha(double pi) {
        double accuracy = Math.min(1.0, Math.abs(Math.PI - pi));
        double accuracyFactor = Math.log10(LeinbnitzActivity.ACCURACY / accuracy) / Math.log10(LeinbnitzActivity.ACCURACY);
        return 1 - Math.min(1, (float) (1 * accuracyFactor));
    }
}
