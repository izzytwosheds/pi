package com.twosheds.pi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class GraphView extends View {
    private Paint paintSquare;
    private Paint paintCircle;

    private float radius;
    private Point center;

    private ArrayList<DrawPoint> points;

    private class DrawPoint {
        private int x;
        private int y;
        private boolean isInside;

        DrawPoint(int x, int y, boolean isInside) {
            this.x = x;
            this.y = y;
            this.isInside = isInside;
        }
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        points = new ArrayList<>(32000);

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

        for (DrawPoint point:points) {
            if (point.isInside) {
                canvas.drawCircle(point.x, point.y, 10, paintCircle);
            } else {
                canvas.drawCircle(point.x, point.y, 10, paintSquare);
            }
        }

    }

    void drawPoint(double x, double y, boolean isInside) {
        int drawX = (int) (radius * x) + center.x;
        int drawY = (int) (radius * y) + center.y;
        DrawPoint point = new DrawPoint(drawX, drawY, isInside);
        points.add(point);
        postInvalidate();
    }
}
