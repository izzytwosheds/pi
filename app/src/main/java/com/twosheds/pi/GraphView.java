package com.twosheds.pi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.ArrayBlockingQueue;

public class GraphView extends View {
    private Paint paintSquare;
    private Paint paintCircle;

    private float radius;
    private Point center;

    private ArrayBlockingQueue<Point> drawQueue;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawQueue = new ArrayBlockingQueue<Point>(20);

        // TODO: move to style
        paintSquare = new Paint();
        paintSquare.setColor(Color.BLUE);
        paintSquare.setStrokeWidth(3.0f);
        paintSquare.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        paintCircle.setStrokeWidth(3.0f);
        paintCircle.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        radius = Math.min(w, h)/2;
        center = new Point(w/2, h/2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(center.x-radius, center.y-radius, center.x+radius, center.y+radius, paintSquare);
        canvas.drawCircle(center.x, center.y, radius, paintCircle);

        try {
            while (!drawQueue.isEmpty()) {
                Point point = drawQueue.take();
                canvas.drawPoint(point.x, point.y, paintCircle);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void drawPoint(double x, double y, boolean isInside) {
        int drawX = (int) (radius * x) + center.x;
        int drawY = (int) (radius * y) + center.y;
        Point point = new Point(drawX, drawY);
        drawQueue.add(point);
        invalidate();
    }
}
