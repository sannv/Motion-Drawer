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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

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
    public boolean addXY, addXsubY, subXaddY, subXY;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float motionX, motionY, touchX, touchY, newX, newY;
    private ArrayList<DrawPoint> drawPointList;
    private static final float MOTION_DRAW_DISTANCE = 8;


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

        drawPointList = new ArrayList<DrawPoint>();

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
//        motionX = getMotionX();
//        motionY = getMotionY();

        if (useBrush == true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    Log.d("onTouchEvent", "the x and y are :" + touchX + " " + touchY);
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
                    storeSenData(touchX, touchY);
//                    drawing1();
                    break;
                case MotionEvent.ACTION_UP:
                    useMotion = false;
                    drawing();
                    break;
                default:
                    return false;
            }
        }

        invalidate();
        return true;
    }

//    public void onSensorChange(float x, float y) {
//        if(useMotion==true) {
//                motionX = x;
//                motionY = y;
//                newX = touchX + motionX ;
//                newY = touchY + motionY ;
//                drawing();
//            setColor("#FF0000FF");
//
//        }
//    }

    public void storeSenData(float senX, float senY){
        if(useMotion == true) {
//            motionX = senX;
//            motionY = senY;
            drawPointList.add(new DrawPoint(senX, senY));
        }

    }

    public void drawing1(){
        if(addXY){
            storeSenData(MOTION_DRAW_DISTANCE, MOTION_DRAW_DISTANCE);
        }
        else if(subXY){
            storeSenData(-MOTION_DRAW_DISTANCE, -MOTION_DRAW_DISTANCE);
        }
        else if(addXsubY){
            storeSenData( MOTION_DRAW_DISTANCE, -MOTION_DRAW_DISTANCE);
        }
        else if(subXaddY){
            storeSenData( -MOTION_DRAW_DISTANCE , MOTION_DRAW_DISTANCE);
        }

    }

    public void drawing(){
        drawPath.moveTo(touchX, touchY);
        Log.d("the array", "the size is " + drawPointList.size());
        float tempx = touchX;
        float tempy = touchY;
//        Log.d("drawing ", "where it starts " + tempx + " " + tempy);
        for(int x=0; x < drawPointList.size(); x++) {
            if (x == 2) {
                x++;
            } else {
                Log.d("drawing ", "wtf " + drawPointList.get(x).getX() + " " + drawPointList.get(x).getY());
                if (drawPointList.get(x).getX() <= MOTION_DRAW_DISTANCE ||
                        drawPointList.get(x).getY() <= MOTION_DRAW_DISTANCE) {
                    float dpx, dpy;
                    dpx = drawPointList.get(x).getX();
                    dpy = drawPointList.get(x).getY();
                    tempx = tempx + dpx;
                    tempy = tempy + dpy;

                    drawPath.lineTo(tempx, tempy);
                }
                else{

                }
            }
        }

        drawPointList.clear();
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
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

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
