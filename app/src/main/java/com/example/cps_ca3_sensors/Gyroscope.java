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

public class Gyroscope extends AppCompatActivity implements SensorEventListener{

    ImageView ball_view;
    Timer timer;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    private SensorManager mSensorManager;
    private Sensor gyroscope_sensor;
    Ball ball;


    private void setBallPosition(double x, double y){

        ball_view.setX((float)x);
        ball_view.setY((float)y);
    }

    private void showBall() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Button btn = (Button) findViewById(R.id.start_game);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float x0 = getRandomNumber(ball_view.getWidth() + 5 , Config.screenWidth - ball_view.getWidth() -5);
                        float y0 = getRandomNumber(ball_view.getHeight() + 5 , Config.screenHeight - ball_view.getHeight() -5) ;
                        ball.setX(x0);
                        ball.setY(y0);
                        ball.initVelocity(5,20);

                    }
                });
                ball.updateBallPosition();

                setBallPosition(ball.getX(), ball.getY());

            }
        }, 0, 10);//Update button every second
    }

    public float getRandomNumber(int low, int high){

        return new Random().nextInt(high - low ) + low;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        final ConstraintLayout content =(ConstraintLayout) findViewById(R.id.activity_gyroscope);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                Config.screenHeight = content.getHeight();
                Config.screenWidth = content.getWidth();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                ball_view = (ImageView) findViewById(R.id.ball);

                float x0 = getRandomNumber(ball_view.getWidth() + 5 , Config.screenWidth - ball_view.getWidth() -5);
                float y0 = getRandomNumber(ball_view.getHeight() + 5 , Config.screenHeight - ball_view.getHeight() -5) ;
                ball = new Ball(x0,y0,ball_view.getWidth(),ball_view.getHeight());
                showBall();
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

        if(gyroscope_sensor != null){
            float gx = event.values[0];
            float gy = event.values[1];
            float gz = event.values[2] ;

            if(ball!=null){

                ball.gyroUpdate(gx * 1 ,gy * 1,gz * 1);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // normal -
    }
}

