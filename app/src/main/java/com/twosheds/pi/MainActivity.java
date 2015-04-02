package com.twosheds.pi;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends ActionBarActivity {
    private static final double PRECISION = 0.00001;
    private static final int EVENT_NEW_VALUE = 1;

    private Random random;

    private static int countInside;
    private static int countTotal;
    private static boolean isRunning;

    private GraphView graphView;
    private TextView piView;
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = (GraphView) findViewById(R.id.graph);
        piView = (TextView) findViewById(R.id.pi);
        stepView = (TextView) findViewById(R.id.steps);
        startButton = (Button) findViewById(R.id.button_start);

        countTotal = 0;
        countInside = 0;

        random = new Random();
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
            isRunning = false;
            startButton.setText(R.string.action_start);
            countTotal = 0;
            countInside = 0;
            graphView.clearPoints();
        } else {
            isRunning = true;
            startButton.setText(R.string.action_stop);
            Thread drawThread = new Thread() {
                @Override
                public void run() {
                    double oldPi = 10.0;
                    double pi = 20.0;
                    while (isRunning && Math.abs(oldPi - pi) > PRECISION) {
                        double x = random.nextDouble() * 2 - 1;
                        double y = random.nextDouble() * 2 - 1;
                        double distance = Math.sqrt(x * x + y * y);
                        boolean isInside = distance < 1;
                        if (isInside) {
                            countInside++;
                        }
                        countTotal++;

                        if (countTotal != countInside) {
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
                }
            };
            drawThread.start();
        }
    }

    private void updateViews(double pi) {
        piView.setText(getString(R.string.pi, pi));
        stepView.setText(getString(R.string.iteration, countTotal));
    }
}
