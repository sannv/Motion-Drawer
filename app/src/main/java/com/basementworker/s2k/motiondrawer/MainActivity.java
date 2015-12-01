package com.basementworker.s2k.motiondrawer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private DrawingView drawingView;
    private Button currPaint;
    private Button motionDraw, brushDraw, saveButton;
    private int rad = 0;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float motionX, motionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView)findViewById(R.id.drawing);
        currPaint = (Button)findViewById(R.id.current_paint_color);
        motionDraw = (Button)findViewById(R.id.motion_paint_btn);
        motionDraw.setOnClickListener(this);
        brushDraw = (Button)findViewById(R.id.draw_btn);
        brushDraw.setOnClickListener(this);
        saveButton = (Button)findViewById(R.id.save_btn);
        saveButton.setOnClickListener(this);
        senSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        drawingView.setUpSensor(senSensorManager, senAccelerometer);
    }

    @Override
    public void onClick( View view ){
        if(view.getId()==R.id.motion_paint_btn){
            drawingView.useMotionDraw();
        }
        else if(view.getId()==R.id.draw_btn){
            drawingView.useBrushDraw();
        }
        else if(view.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawingView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawingView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawingView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
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

    /*
    The method will let the user choose a color with the color picker app
    TODO set intent and get back color
    TODO set the color to curr_paint. color should be a string #FFXXXXXX
     */
    public void paintClicked(View view){
        if(rad == 0) {
            currPaint = (Button) findViewById(R.id.current_paint_color);
//        currPaint.setBackground("FF00FF00");
            //fill in
            int c = Color.parseColor("#FF00FF00");
            drawingView.setColor("#FF00FF00");//this is here for testing
            currPaint.setTag("#FF00FF00");
            currPaint.setBackgroundColor(c);
            rad = 1;
        }
        else if(rad == 1){
            drawingView.setColor("#FF000000");
            currPaint.setTag("#FF000000");
            currPaint.setBackgroundColor((int)Color.parseColor("#FF000000"));
            rad = 0;
        }
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
                    drawingView.onSensorChange(motionX, motionY);
                }

//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
