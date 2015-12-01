package com.basementworker.s2k.motiondrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by s2k on 11/16/2015.
 */

public class DrawingView extends View implements SensorEventListener{
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private boolean useMotion, useBrush;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float motionX, motionY, touchX, touchY, newX, newY;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        useMotion = false;
        useBrush = true;
    }

    private void setupDrawing(){
        //setup drawing area
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        motionX = getMotionX();
        motionY = getMotionY();

        if (useBrush == true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }

        }
        if(useMotion == true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = event.getX();
                    touchY = event.getY();
                    drawPath.moveTo(motionX, motionY);
//                    drawPath.reset();
                    drawPath.lineTo(motionX, motionY);
                    drawing();
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
        }

        invalidate();
        return true;
    }

    public void onSensorChange(float x, float y) {
        if(useMotion==true) {
                motionX = x;
                motionY = y;
                newX = touchX + motionX ;
                newY = touchY + motionY ;
                drawing();
            setColor("#FF0000FF");

        }
    }


    public void drawing(){
        drawPath.moveTo(touchX, touchY);
        drawPath.moveTo(newX, newY);
        drawCanvas.drawPath(drawPath, drawPaint);
        touchX =+newX;
        touchY =+newY;
//        drawPath.reset();

        invalidate();
    }

    public float getMotionX(){
        return motionX;
    }

    public float getMotionY(){
        return motionY;
    }

    public void setUpSensor(SensorManager senMan, Sensor senAcc){
        senSensorManager = senMan;
        senAccelerometer = senAcc;
    }

    public void useMotionDraw(){
        invalidate();
        useMotion = true;
        useBrush = false;

    }

    public void useBrushDraw(){
        invalidate();
        useBrush = true;
        useMotion = false;

    }

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        Sensor mySensor = sensorEvent.sensor;
        motionX = event.values[0];
        motionY = event.values[1];
        float newX;
        float newY;
//            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                motionX = event.values[0];
//                motionY = event.values[1];
        newX = event.values[0];
        newY = event.values[1];
        if(Math.abs(newX - motionX) >= 0 || Math.abs(newY - motionY) > 0) {
            onSensorChange(motionX, motionY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
