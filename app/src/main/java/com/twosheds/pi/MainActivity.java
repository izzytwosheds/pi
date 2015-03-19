package com.twosheds.pi;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;

public class MainActivity extends ActionBarActivity {
    private Random random;

    private int countInside;
    private int countOutside;
    private double pi;

    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = (GraphView) findViewById(R.id.graph);

        random = new Random();
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
        Thread drawThread = new Thread() {
            @Override
            public void run() {
                for (int i=0; i<10; i++) {
                    double x = random.nextDouble() * 2 - 1;
                    double y = random.nextDouble() * 2 - 1;
                    double distance = Math.sqrt(x * x + y * y);
                    graphView.drawPoint(x, y, distance <= 1);
                }
            }
        };
        drawThread.start();
    }
}
