package com.twosheds.pi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {
    private Paint paintBackground;
    private Paint paintSquare;
    private Paint paintCircle;

    private float radius;
    private Point center;

    private Bitmap bitmap;
    private Canvas canvas;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setColor(getContext().getResources().getColor(R.color.background));
        paintBackground.setStyle(Paint.Style.FILL);

        paintSquare = new Paint();
        paintSquare.setColor(getContext().getResources().getColor(R.color.square));
        paintSquare.setAntiAlias(true);

        paintCircle = new Paint();
        paintCircle.setColor(getContext().getResources().getColor(R.color.cicle));
        paintCircle.setAntiAlias(true);
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
        canvas.drawCircle(drawX, drawY, 2, isInside ? paintCircle : paintSquare);
        postInvalidate();
    }

    void clearPoints() {
        resetCanvas();
    }

    private void resetCanvas() {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);

        paintSquare.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(5);
        canvas.drawRect(0, 0, radius * 2, radius * 2, paintSquare);
        paintSquare.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircle.setStrokeWidth(1);

        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(5);
        canvas.drawCircle(radius, radius, radius, paintCircle);
        paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircle.setStrokeWidth(1);
    }

    void drawIcon() {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);

        paintSquare.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircle.setStrokeWidth(1);
        canvas.drawRect(0, 0, radius * 2, radius * 2, paintSquare);

        paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircle.setStrokeWidth(1);
        canvas.drawCircle(radius, radius, radius, paintCircle);
    }
}
