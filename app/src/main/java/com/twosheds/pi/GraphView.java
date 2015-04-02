package com.twosheds.pi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {
    private Paint paintBackground;
    private Paint paintSquare;
    private Paint paintCircle;
    private Paint paintPointInside;
    private Paint paintPointOutside;

    private float radius;
    private Point center;

    private Bitmap bitmap;
    private Canvas canvas;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO: move to style
        paintBackground = new Paint();
        paintBackground.setColor(Color.BLACK);
        paintBackground.setStyle(Paint.Style.FILL);

        paintSquare = new Paint();
        paintSquare.setColor(Color.BLUE);
        paintSquare.setStrokeWidth(3.0f);
        paintSquare.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        paintCircle.setStrokeWidth(3.0f);
        paintCircle.setStyle(Paint.Style.STROKE);

        paintPointInside = new Paint();
        paintPointInside.setColor(Color.RED);
        paintPointInside.setStyle(Paint.Style.FILL_AND_STROKE);

        paintPointOutside = new Paint();
        paintPointOutside.setColor(Color.BLUE);
        paintPointOutside.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        radius = Math.min(w, h)/2;
        center = new Point(w/2, h/2);

        // TODO: handle existing bitmap
        bitmap = Bitmap.createBitmap((int)radius*2, (int)radius*2, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        resetCanvas();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, center.x - radius, center.y - radius, null);
    }

    void drawPoint(double x, double y, boolean isInside) {
        int drawX = (int) (radius * x + radius);
        int drawY = (int) (radius * y + radius);
        canvas.drawCircle(drawX, drawY, 2, isInside ? paintPointInside : paintPointOutside);
        postInvalidate();
    }

    void clearPoints() {
        resetCanvas();
    }

    private void resetCanvas() {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);
        canvas.drawRect(0, 0, radius*2, radius*2, paintSquare);
        canvas.drawCircle(radius, radius, radius, paintCircle);
    }
}
