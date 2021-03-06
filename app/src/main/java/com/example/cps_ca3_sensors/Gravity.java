package com.example.cps_ca3_sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Gravity extends AppCompatActivity implements SensorEventListener{

    ImageView ball_view;
    Timer timer;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    private SensorManager mSensorManager;
    private Sensor gravity_sensor;
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

                        float x0 = getRandomNumber(Math.round(Config.screenWidth/2) - 2 * ball_view.getWidth() , Math.round(Config.screenWidth/2) + 2 * ball_view.getWidth() );
                        float y0 = getRandomNumber(Math.round(Config.screenHeight/2) - 2 * ball_view.getHeight() , Math.round(Config.screenHeight/2) +  2 * ball_view.getHeight()) ;

                        ball.setX(x0);
                        ball.setY(y0);
                        ball.initVelocity(6,12);

                    }
                });
                ball.updateBallPosition();

                setBallPosition(ball.getX(), ball.getY());

            }
        }, 0, 5);      //Update button every 10 second
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
        gravity_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

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

                float x0 = getRandomNumber(Math.round(Config.screenWidth/2) - 2 * ball_view.getWidth() , Math.round(Config.screenWidth/2) + 2 * ball_view.getWidth() );
                float y0 = getRandomNumber(Math.round(Config.screenHeight/2) - 2 * ball_view.getHeight() , Math.round(Config.screenHeight/2) + 2 * ball_view.getHeight()) ;
                ball = new Ball(x0,y0,ball_view.getWidth(),ball_view.getHeight());
                showBall();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ball != null)
            showBall();

        if (gravity_sensor != null) {
            mSensorManager.registerListener(this, gravity_sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gravity_sensor != null) {
            mSensorManager.unregisterListener(this);
        }
        timer.cancel();
        timer.purge();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(gravity_sensor != null){
            double gx = -event.values[0];
            double gy = event.values[1];
            double gz = event.values[2] ;

            if(ball!=null)
                ball.updateGravity(gx,gy,gz);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

