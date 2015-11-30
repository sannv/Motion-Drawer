package com.basementworker.s2k.motiondrawer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private DrawingView drawingView;
    private Button currPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView)findViewById(R.id.drawing);
        currPaint = (Button)findViewById(R.id.current_paint_color);
//        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

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
        currPaint = (Button)findViewById(R.id.current_paint_color);
//        currPaint.setBackground("FF00FF00");
        //fill in
        int c = Color.parseColor("#FF00FF00");
        drawingView.setColor("#FF00FF00");//this is here for testing
        currPaint.setTag("#FF00FF00");
        currPaint.setBackgroundColor(c);
    }
}
