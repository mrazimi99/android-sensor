package com.example.cps_ca3_sensors;

import android.widget.ImageView;

import java.util.Random;



public class Ball {
    public double vx;
    public double vy;
    public double x;
    public double y;
    public double ax;
    public double ay;
    public float width;
    public float height;

    public double gx,gy,gz;


    public Ball(double x,double y,float width,float height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        initVelocity(5,20);
    }

    public void initVelocity(int low,int high){
        vx = new Random().nextInt(high - low) + low;
        vy = new Random().nextInt(high - low) + low;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean hitWall(){
        if(x >= (Config.screenWidth - width) || x <= 0){
            return true;
        }

        if(y >= (Config.screenHeight - height) || y <= 0){
            return true;
        }

        return false;
    }

    public String getWall(){
        int wall_num = 0;
        String wall="";
        double _x = 0, _y = 0;
        if( y >=Config.screenHeight - height ) {
            wall_num += 1;
            wall = "down";
            _y = Config.screenHeight - height - 1;
        }

        if(y<=0) {
            wall_num += 1;
            wall = "up";
            _y =  1;
        }

        if(x >= Config.screenWidth - width) {
            wall_num += 1;
            wall = "right";
            _x = Config.screenWidth - width - 1;
        }

        if(x<=0) {
            wall_num += 1;
            wall = "left";
            _x = 1;
        }

        if(wall_num>1){

            x = _x;
            y = _y;



            return "corner";
        }

        return wall;
    }

    public boolean isSlip(String wall){
        if(wall.equals("left") || wall.equals("right")){
            if(vx<Config.epsilon && vx>-Config.epsilon){
                return true;
            }
            return false;
        }

        if(wall.equals("up") || wall.equals("down")){
            if(vy<Config.epsilon && vy>-Config.epsilon){
                return true;
            }
            return false;
        }
        return false;
    }

    public double findN(String wall){
        if(wall.equals("up")){
            if(gy>0)
                return 0;
            else
                return Config.m * gy;
        }

        if(wall.equals("down")){
            if(gy>0)
                return  -Config.m * gy;
            else
                return 0;
        }

        if(wall.equals("right")){
            if(gx>0)
                return Config.m * gx;
            else
                return 0;
        }

        if(wall.equals("left")){
            if(gx<0)
                return -Config.m * gx;
            else
                return 0;
        }
        return 0;

    }


    public void handleSlip(String wall){
        double N = findN(wall);
        double fx = 0;
        double fy = 0;
        System.out.println("handleslip");
        if(wall.equals("up") || wall.equals("down")){
            if(vy <Config.epsilon && vy>-Config.epsilon){
                if(Math.abs(Config.m*gx)>Math.abs(Config.uS*N)){
                    System.out.println("1");
                    fx = (vx > 0) ? Config.m * gx - Config.uK * N :(vx < 0)? Config.m * gx + Config.uK * N :
                            (gx > 0) ? Config.m * gx - Config.uS * N : Config.m * gx + Config.uS * N;

                    fy = (N == 0 )? Config.m*gy : 0;
                }
                else {
                    System.out.println("2");
                    fx = 0;
                    fy = (N == 0)? Config.m*gy : 0;
                }
            }

        }

        if(wall.equals("right") || wall.equals("left")){
            if(vx <Config.epsilon && vx>-Config.epsilon){
                if(Math.abs(Config.m*gy)>Math.abs(Config.uS*N)){
                    fy = (vy > 0) ? Config.m * gy - Config.uK * N :(vy < 0)? Config.m * gy + Config.uK * N :
                            (gy > 0) ? Config.m * gy - Config.uS * N : Config.m * gy + Config.uS * N;

                    fx = (N == 0)? Config.m*gx : 0;
                }
                else {
                    fy = 0;
                    fx = (N == 0)? Config.m*gx : 0;
                }
            }
        }

        ax = fx / Config.m;
        ay = fy / Config.m ;
        if(fx == 0)
            vx = 0;
        else
            vx = ax * 10 * Config.MS2S +vx;
        if(fy == 0 )
            vy = 0 ;
        else
            vy = ay * 10 * Config.MS2S +vy;
        //System.out.println("fx "+fx+" fy "+fy+" n "+N);

    }

    public void handleReflect(String wall){
        double v1 = Math.sqrt(vx*vx + vy*vy);
        double v2 = Math.sqrt(0.9 *v1*v1);
        if(wall.equals("right") || wall.equals("left")){
            vx = -(vx/Math.abs(vx))*v2*(Math.abs(vx/v1));
            vy = (vy/Math.abs(vy))*v2*(Math.abs(vy/v1));
        }
        if(wall.equals("up") || wall.equals("down")){
            vx = (vx/Math.abs(vx))*v2*(Math.abs(vx/v1));
            vy = -(vy/Math.abs(vy))*v2*(Math.abs(vy/v1));
        }
    }


    public void updateBallPosition(){

        double v0x = vx;
        double v0y = vy;

        if(hitWall()){
            String wall = getWall();
            if(!wall.equals("corner")){
                if(isSlip(wall)){
                    handleSlip(wall);
                }
                else{
                    handleReflect(wall);
                    v0x = vx;
                    v0y = vy;

                }

            }
            else{

                vx = 0;
                vy = 0;
                ax = 0;
                ay = 0;
//                double fx = Config.m * gx;
//                double fy = Config.m * gy;
//                ax = fx / Config.m;
//                ay = fy / Config.m;
//                vx = ax  10  Config.MS2S +vx;
//                vy = ay  10  Config.MS2S +vy;
            }

        }
        else{

            double fx = Config.m * gx;
            double fy = Config.m * gy;
            ax = fx / Config.m;
            ay = fy / Config.m;
            vx = ax * 10 * Config.MS2S +vx;
            vy = ay * 10 * Config.MS2S +vy;

        }

        x += 0.5*ax*(10*Config.MS2S)*(10*Config.MS2S)+v0x*(10*Config.MS2S)*250;
        y += 0.5*ay*(10*Config.MS2S)*(10*Config.MS2S)+v0y*(10*Config.MS2S)*250;



    }

    public void updateBallWithGravity(double gx,double gy,double gz){
        this.gx = gx;
        this.gy = gy;
        this.gz = gz;

    }
}
