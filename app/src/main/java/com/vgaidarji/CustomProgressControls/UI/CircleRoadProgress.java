package com.vgaidarji.CustomProgressControls.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import com.vgaidarji.CustomProgressControls.R;

public class CircleRoadProgress extends View {
    private float circleCenterPointX;
    private float circleCenterPointY;
    private int roadColor;
    private float roadStrokeWidth;
    private float roadRadius;
    private int roadInnerCircleColor;
    private float roadInnerCircleStrokeWidth;
    private float roadInnerCircleRadius;
    private int roadOuterCircleColor;
    private float roadOuterCircleStrokeWidth;
    private float roadOuterCircleRadius;
    private int arcLoadingColor;
    private float arcLoadingStrokeWidth;
    private float arcLoadingDashLength;
    private float arcLoadingDistanceBetweenDashes;
    private float arcLoadingStartAngle;
    private float textLocationX;
    private float textLocationY;
    private int textColor;
    private float textSize;

    private int percent;

    public Handler uiThread = new Handler();

    public CircleRoadProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleCenterPointX = w / 2;
        circleCenterPointY = h / 2;

        int paddingInContainer = 3;
        roadRadius = (w / 2) - (roadStrokeWidth / 2) - paddingInContainer;

        int innerCirclesPadding = 3;
        roadOuterCircleRadius = (w / 2) - paddingInContainer -
                (roadOuterCircleStrokeWidth / 2) - innerCirclesPadding;

        roadInnerCircleRadius = roadRadius - (roadStrokeWidth / 2)
                + (roadInnerCircleStrokeWidth / 2) + innerCirclesPadding;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG |
                Paint.DITHER_FLAG |
                Paint.ANTI_ALIAS_FLAG);

        drawRoad(paint, canvas);
        drawRoadInnerCircle(paint, canvas);
        drawRoadOuterCircle(paint, canvas);
        drawArcLoading(paint, canvas);
        drawPercents(canvas);
    }

    private void initializeAttributes(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleRoadProgressWidget);
        circleCenterPointX = ta.getFloat(R.styleable.CircleRoadProgressWidget_circleCenterPointX, 54f);
        circleCenterPointY = ta.getFloat(R.styleable.CircleRoadProgressWidget_circleCenterPointY, 54f);
        roadColor = ta.getColor(R.styleable.CircleRoadProgressWidget_roadColor, Color.parseColor("#575757"));
        roadStrokeWidth = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadStrokeWidth, 20f);
        roadRadius = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadRadius, 42f);
        roadInnerCircleColor = ta.getColor(R.styleable.CircleRoadProgressWidget_roadInnerCircleColor, Color.parseColor("#ffffff"));
        roadInnerCircleStrokeWidth = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadInnerCircleStrokeWidth, 1f);
        roadInnerCircleRadius = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadInnerCircleRadius, 42f);
        roadOuterCircleColor = ta.getColor(R.styleable.CircleRoadProgressWidget_roadOuterCircleColor, Color.parseColor("#ffffff"));
        roadOuterCircleStrokeWidth = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadOuterCircleStrokeWidth, 1f);
        roadOuterCircleRadius = ta.getFloat(R.styleable.CircleRoadProgressWidget_roadOuterCircleRadius, 42f);
        arcLoadingColor = ta.getColor(R.styleable.CircleRoadProgressWidget_arcLoadingColor, Color.parseColor("#f5d600"));
        arcLoadingStrokeWidth = ta.getFloat(R.styleable.CircleRoadProgressWidget_arcLoadingStrokeWidth, 3f);
        arcLoadingDashLength = ta.getFloat(R.styleable.CircleRoadProgressWidget_arcLoadingDashLength, 10f);
        arcLoadingDistanceBetweenDashes = ta.getFloat(R.styleable.CircleRoadProgressWidget_arcLoadingDistanceBetweenDashes, 5f);
        arcLoadingStartAngle = ta.getFloat(R.styleable.CircleRoadProgressWidget_arcLoadingStartAngle, 270f);
        textLocationX = ta.getFloat(R.styleable.CircleRoadProgressWidget_textLocationX, 54f / 2f);
        textLocationY = ta.getFloat(R.styleable.CircleRoadProgressWidget_textLocationY, 54f / 2f);
        textColor = ta.getColor(R.styleable.CircleRoadProgressWidget_textColor, Color.parseColor("#ffffff"));
        textSize = ta.getFloat(R.styleable.CircleRoadProgressWidget_textSize, 20f);
        ta.recycle();

    }

    private void drawRoad(Paint paint, Canvas canvas){
        paint.setDither(true);
        paint.setColor(roadColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roadStrokeWidth);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, roadRadius, paint);
    }

    private void drawRoadInnerCircle(Paint paint, Canvas canvas){
        paint.setDither(true);
        paint.setColor(roadInnerCircleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roadInnerCircleStrokeWidth);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, roadInnerCircleRadius, paint);
    }

    private void drawRoadOuterCircle(Paint paint, Canvas canvas){
        paint.setDither(true);
        paint.setColor(roadOuterCircleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roadOuterCircleStrokeWidth);
        canvas.drawCircle(circleCenterPointX, circleCenterPointY, roadOuterCircleRadius, paint);
    }

    private void drawArcLoading(Paint paint, Canvas canvas){
        paint.setColor(arcLoadingColor);
        paint.setStrokeWidth(arcLoadingStrokeWidth);
        paint.setPathEffect(new DashPathEffect(new float[] {arcLoadingDashLength, arcLoadingDistanceBetweenDashes}, 0));
        float delta = circleCenterPointX - roadRadius;
        float arcSize = (circleCenterPointX - (delta / 2f)) * 2f;
        RectF box = new RectF(delta, delta, arcSize, arcSize);
        float sweep = 360 * percent * 0.01f;
        canvas.drawArc(box, arcLoadingStartAngle, sweep, false, paint);
    }

    private void drawPercents(Canvas canvas){
        String percentsString = new StringBuilder().append(String.valueOf(percent)).append("%").toString();
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        int positionX = (getMeasuredWidth() / 2);
        int positionY = (int) ((getMeasuredHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(percentsString, positionX, positionY, textPaint);
    }

    public void changePercentage(int percent){
        this.percent = percent;
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }
}