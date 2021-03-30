package com.example.cps_ca3_sensors;

import android.widget.ImageView;

import java.util.Random;

public class Ball {
    public float vx;
    public float vy;
    public float x;
    public float y;
    public float ax;
    public float ay;
    ImageView my_ball;
    public float width;
    public float height;
    public Ball(float x,float y,float width,float height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        initVelocity();
    }

    public void initVelocity(){
        vx = new Random().nextInt(600) + 300;
        vy = new Random().nextInt(600) + 600;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void updateBallPosition(){
        if(x >= (Config.screenWidth - width) || x <= 0){
            vx = -vx;
        }

        if(y >= (Config.screenHeight - height) || y <= 0){
            vy = - vy;
        }
        x += vx * 10 * Config.MS2S;
        y += vy * 10 *Config.MS2S;

    }
}
