package com.rabahdiallo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by B-RABAH on 09/03/2016.
 */
public class CaricatureView extends SurfaceView implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {
    private Context appContext;
    private SurfaceHolder holder;
    private int[] xFace=new int[10];
    private int[] yFace=new int[10];
    private int xNoiseTop,yNoiseTop,xNoiseBottom,yNoiseBottom,xNoiseRight,yNoiseRight,xNoiseLeft,yNoiseLeft;
    private int xEyeRight,yEyeRight,xEyeLeft,yEyeLeft;
    private int xTouthRight,yTouthRight,xTouthLeft,yTouthLeft;
    public CaricatureView(Context context) {
        super(context);
        appContext = context;
        holder =  getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    public void drawFace(){

    }

    @Override
    public void run() {

    }
}
