package com.example.cps_ca3_sensors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class gyroscope_app extends AppCompatActivity implements SensorEventListener{

    ImageView ballButton;
    Timer timer;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    int screenWidth, screenHeight, initWidth, initHeight, curWidth, curHeight;
    boolean xClockWise, yClockWise;
    int vx, vy;

    private SensorManager mSensorManager;
    private Sensor gyroscope_sensor;


    private void setButtonRandomPosition(ImageView button, int x, int y){

        button.setX(x);

        button.setY(y);
    }

    private void startRandomButton(final ImageView button, final int w, final int h) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(curWidth >= (screenWidth - w) || curWidth <= 0){
                    vx = -vx;
                }

                if(curHeight >= (screenHeight - h) || curHeight <= 0){
                    vy = - vy;
                }
                curWidth += vx * 10;
                curHeight += vy * 10;

                setButtonRandomPosition(button, curWidth, curHeight);

            }
        }, 0, 10);//Update button every second
    }

    public void init(int width, int high){


        int hx = screenWidth - width - 5;
        int lx = width + 5;

        int hy = screenHeight - high - 5;
        int ly = high + 5;
        initWidth = new Random().nextInt(hx - lx ) + lx;
        initHeight = new Random().nextInt(hy - ly ) + ly;
        curHeight = initHeight;
        curWidth = initWidth;

        vx = 1;
        vy = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_app);
        final ConstraintLayout content =(ConstraintLayout) findViewById(R.id.activity_gyroscope_app);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int w = content.getWidth();
                int h = content.getHeight();
                screenWidth = w;
                screenHeight = h;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                ballButton = (ImageView) findViewById(R.id.ball);
                init(ballButton.getWidth(),ballButton.getHeight());

                ballButton.setX(curWidth);
                ballButton.setY(curHeight);
                startRandomButton(ballButton,ballButton.getWidth(),ballButton.getHeight() );
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyroscope_sensor != null) {
            mSensorManager.registerListener(this, gyroscope_sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gyroscope_sensor != null) {
            mSensorManager.unregisterListener(this);
        }
        timer.cancel();
        timer.purge();

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("MY_APP", event.toString());
        if(gyroscope_sensor != null){

            System.out.println("changed !");
            if(event.values[0] != 0.0){
                System.out.println("x");
                System.out.println(event.values[0]);
            }
            if(event.values[1] != 0.0){
                System.out.println("y");
                System.out.println(event.values[1]);
            }
            if(event.values[2] != 0.0){
                System.out.println("z");
                System.out.println(event.values[2]);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("MY_APP", sensor.toString() + " - " + accuracy);
    }
}

