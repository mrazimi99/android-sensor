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

              /* if(curWidth >= (screenWidth - w) || curWidth <= 0){
                    vx = -vx;
                }

                if(curHeight >= (screenHeight - h) || curHeight <= 0){
                    vy = - vy;
                }
                curWidth += vx * 10;
                curHeight += vy * 10;*/

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
        setContentView(R.layout.activity_gyroscope_app);
        final ConstraintLayout content =(ConstraintLayout) findViewById(R.id.activity_gyroscope_app);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);


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
                ball = new Ball(200,100,ball_view.getWidth(),ball_view.getHeight());
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
        //Log.d("MY_APP", event.toString());
        //if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
           // return;
        if(gyroscope_sensor != null){
            System.out.println("sensor data");
            double gx = -event.values[0];
            double gy = event.values[1];
            double gz = event.values[2] ;

            System.out.println(gx);
            System.out.println(gy);
            System.out.println(gz);
            System.out.println("---------------------------------");

            if(ball!=null)
                ball.updateBallWithGravity(gx,gy,gz);






            }

            /*System.out.println(event.values[0]);
            System.out.println(event.values[1]);
            System.out.println(event.values[2]);*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
       // Log.d("MY_APP", sensor.toString() + " - " + accuracy);
    }
}

