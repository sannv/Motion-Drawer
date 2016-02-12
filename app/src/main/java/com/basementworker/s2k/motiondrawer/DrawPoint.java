package com.basementworker.s2k.motiondrawer;

/**
 * Created by s2k on 12/1/2015.
 */
public class DrawPoint {
    private float x, y;

    public DrawPoint(){
        x=0;
        y=0;
    }

    public DrawPoint(float x, float y){
        this.x=x;
        this.y=y;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x=x;
    }

    public void setY(float y){
        this.y=y;
    }
}
