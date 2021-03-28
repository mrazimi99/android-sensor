package com.example.cps_ca3_sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Real Dim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, gyroscope_app.class);
//        ViewGroup x = findViewById(R.id.activity_gyroscope_app);
//        if(x == null)
//            System.out.println("hoo");
//        Log.d(TAG, "width " + x.getMeasuredWidth() );
//        Log.d(TAG, "height " + x.getMeasuredHeight() );
        startActivity(intent);




    }
}

